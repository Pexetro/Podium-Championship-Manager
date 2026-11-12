package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var btEditarSalvar: Button
    private lateinit var btCancelar: Button
    private lateinit var btSair: Button
    private lateinit var tvNomeHeader: TextView
    private lateinit var tvEmailHeader: TextView

    private var isEditando = false
    private var usuarioAtual: Pessoa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializa componentes
        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etSenha = findViewById(R.id.etSenha)
        btEditarSalvar = findViewById(R.id.btEditarSalvar)
        btCancelar = findViewById(R.id.btCancelar)
        btSair = findViewById(R.id.btSair)
        tvNomeHeader = findViewById(R.id.tvNomeHeader)
        tvEmailHeader = findViewById(R.id.tvEmailHeader)

        carregarDadosDoUsuario()

        // Ação do botão Editar/Salvar
        btEditarSalvar.setOnClickListener {
            if (isEditando) {
                salvarAlteracoes()
            } else {
                habilitarModoEdicao()
            }
        }

        // Ação do botão Cancelar
        btCancelar.setOnClickListener {
            cancelarEdicao()
        }

        // Ação do botão Sair
        btSair.setOnClickListener {
            SessionManager(this).logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun carregarDadosDoUsuario() {
        val idUsuario = SessionManager(this).obterIdUsuario()
        if (idUsuario == 0L) return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.buscarPessoa(idUsuario)
                if (response.isSuccessful && response.body() != null) {
                    usuarioAtual = response.body()

                    // Preenche os campos e os cabeçalhos
                    etNome.setText(usuarioAtual?.nome)
                    etEmail.setText(usuarioAtual?.email)

                    tvNomeHeader.text = usuarioAtual?.nome
                    tvEmailHeader.text = usuarioAtual?.email
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun habilitarModoEdicao() {
        isEditando = true

        etNome.isEnabled = true
        etEmail.isEnabled = true
        etSenha.isEnabled = true

        btEditarSalvar.text = "SALVAR ALTERAÇÕES"
        btEditarSalvar.backgroundTintList = getColorStateList(android.R.color.holo_orange_dark)

        btCancelar.visibility = Button.VISIBLE // Mostra o botão cancelar
    }

    private fun cancelarEdicao() {
        isEditando = false

        // Volta os dados originais
        etNome.setText(usuarioAtual?.nome)
        etEmail.setText(usuarioAtual?.email)
        etSenha.setText("") // Limpa o campo de senha

        etNome.isEnabled = false
        etEmail.isEnabled = false
        etSenha.isEnabled = false

        btEditarSalvar.text = "EDITAR PERFIL"
        btEditarSalvar.backgroundTintList = getColorStateList(R.color.primary_green)

        btCancelar.visibility = Button.GONE // Esconde o botão cancelar
    }

    private fun salvarAlteracoes() {
        val idUsuario = SessionManager(this).obterIdUsuario()

        val nome = etNome.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val senha = etSenha.text.toString().trim()

        if (nome.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nome e e-mail são obrigatórios.", Toast.LENGTH_SHORT).show()
            return
        }

        // Se a senha estiver vazia, envia null para o Java não alterar
        val senhaParaEnviar = if (senha.isNotEmpty()) senha else null

        val pessoaAtualizada = Pessoa(
            nome = nome,
            email = email,
            cpf = usuarioAtual?.cpf ?: 0L,
            data_nascimento = usuarioAtual?.data_nascimento ?: "",
            senha = senhaParaEnviar
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.atualizarPessoa(idUsuario, pessoaAtualizada)
                if (response.isSuccessful) {
                    Toast.makeText(this@PerfilActivity, "Perfil atualizado!", Toast.LENGTH_SHORT).show()

                    // Atualiza a tela com os novos dados
                    usuarioAtual = response.body()
                    tvNomeHeader.text = usuarioAtual?.nome
                    tvEmailHeader.text = usuarioAtual?.email

                    cancelarEdicao() // Volta para o modo visualização
                } else {
                    Toast.makeText(this@PerfilActivity, "Erro: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PerfilActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}