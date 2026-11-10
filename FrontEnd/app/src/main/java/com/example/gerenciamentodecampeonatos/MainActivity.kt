package com.example.gerenciamentodecampeonatos


import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gerenciamentodecampeonatos.network.Pessoa
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var etSenha: EditText
    private lateinit var etEmail: EditText
    private lateinit var btCadastro: TextView
    private lateinit var btLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        val session = SessionManager(this)

        if (session.estaLogado()) {
            val intent = Intent(this, HubActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        splashScreen.setKeepOnScreenCondition {
            Thread.sleep(1000)
            false
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etSenha = findViewById(R.id.etSenha)
        etEmail = findViewById(R.id.etEmail)
        btCadastro = findViewById(R.id.btCadastro)
        btLogin = findViewById(R.id.btLogin)

        btCadastro.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()

        }

        btLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = Pessoa (
                email = email,
                senha = senha,
                nome = "",
                cpf = 0,
                data_nascimento = ""
            )

            btLogin.isEnabled = false

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.loginPessoa(loginRequest)

                    if (response.isSuccessful) {
                        val pessoaLogada = response.body()

                        // --- ADICIONE ESTES LOGS ---
                        android.util.Log.d("LOGIN_DEBUG", "Login deu certo. Objeto veio nulo? ${pessoaLogada == null}")
                        android.util.Log.d("LOGIN_DEBUG", "ID recebido do banco: ${pessoaLogada?.id_pessoa}")
                        // ---------------------------

                        if (pessoaLogada != null && pessoaLogada.id_pessoa != null) {
                            val session = SessionManager(this@MainActivity)
                            session.salvarIdUsuario(pessoaLogada.id_pessoa)

                            // --- ADICIONE ESTE LOG ---
                            android.util.Log.d("LOGIN_DEBUG", "ID salvo no SharedPreferences: ${session.obterIdUsuario()}")
                            // -------------------------
                        }

                        Toast.makeText(this@MainActivity, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@MainActivity, HubActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "E-mail ou senha incorretos.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Erro de conexão: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    btLogin.isEnabled = true
                    btLogin.text = "ENTRAR"
                }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}