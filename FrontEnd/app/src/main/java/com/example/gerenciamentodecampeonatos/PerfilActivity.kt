package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.network.Pessoa
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import kotlinx.coroutines.launch

class PerfilActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btEditar: Button
    private lateinit var btSair: Button

    private var isEditando = false
    private var usuarioAtual: Pessoa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        etNome = findViewById(R.id.etNomePerfil)
        etEmail = findViewById(R.id.etEmailPerfil)
        etSenha = findViewById(R.id.etSenhaPerfil)
        btEditar = findViewById(R.id.btEditar)
        btSair = findViewById(R.id.btSair)

        // 1. Busca os dados assim que a tela abre
        carregarDadosDoUsuario()

        // 2. Lógica do botão Editar/Salvar
        btEditar.setOnClickListener {
            if (isEditando) {
                salvarAlteracoes()
            } else {
                habilitarEdicao()
            }
        }

        // 3. Lógica do botão Sair (Logout)
        btSair.setOnClickListener {
            val session = SessionManager(this)
            session.logout() // Limpa o SharedPreferences

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun carregarDadosDoUsuario() {
        val session = SessionManager(this)
        val idUsuario = session.obterIdUsuario()

        if (idUsuario == 0L) {
            Toast.makeText(this, "Sessão expirada.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.buscarPessoa(idUsuario)
                if (response.isSuccessful && response.body() != null) {
                    usuarioAtual = response.body()

                    // Preenche os campos
                    etNome.setText(usuarioAtual?.nome)
                    etEmail.setText(usuarioAtual?.email)
                } else {
                    Toast.makeText(this@PerfilActivity, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun habilitarEdicao() {
        isEditando = true
        etNome.isEnabled = true
        etEmail.isEnabled = true
        etSenha.isEnabled = true

        btEditar.text = "SALVAR ALTERAÇÕES"
        btEditar.backgroundTintList = getColorStateList(android.R.color.holo_orange_dark)
    }

    private fun salvarAlteracoes() {
        val session = SessionManager(this)
        val idUsuario = session.obterIdUsuario()

        val nome = etNome.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val senha = etSenha.text.toString().trim() // Se estiver vazio, não altera

        if (nome.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nome e e-mail não podem ficar em branco.", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria o objeto atualizado (se a senha estiver vazia, não envia)
        val pessoaAtualizada = Pessoa(
            nome = nome,
            email = email,
            cpf = usuarioAtual?.cpf ?: 0L, // Mantém o CPF original
            data_nascimento = usuarioAtual?.data_nascimento ?: "",
            senha = if (senha.isNotEmpty()) senha else null
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.atualizarPessoa(idUsuario, pessoaAtualizada)

                if (response.isSuccessful) {
                    Toast.makeText(this@PerfilActivity, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()

                    // Bloqueia os campos novamente
                    isEditando = false
                    etNome.isEnabled = false
                    etEmail.isEnabled = false
                    etSenha.isEnabled = false
                    etSenha.setText("") // Limpa o campo de senha

                    btEditar.text = "EDITAR PERFIL"
                    btEditar.backgroundTintList = getColorStateList(R.color.primary_green)
                } else {
                    Toast.makeText(this@PerfilActivity, "Erro ao atualizar: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}