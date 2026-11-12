package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.network.Time
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class CriarTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_time)

        val etNome = findViewById<EditText>(R.id.etNomeTime)
        val btCriar = findViewById<MaterialButton>(R.id.btCriarTime)

        btCriar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome do time!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idCriador = SessionManager(this).obterIdUsuario()
            val novoTime = Time(nome_time = nome)

            btCriar.isEnabled = false
            btCriar.text = "Criando..."

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.criarTime(novoTime, idCriador)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CriarTimeActivity, "Time criado!", Toast.LENGTH_SHORT).show()
                        finish() // Volta para a lista
                    } else {
                        Toast.makeText(this@CriarTimeActivity, "Erro: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CriarTimeActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
                } finally {
                    btCriar.isEnabled = true
                    btCriar.text = "Criar Time"
                }
            }
        }
    }
}