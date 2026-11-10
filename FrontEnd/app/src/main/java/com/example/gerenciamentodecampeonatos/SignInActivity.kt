package com.example.gerenciamentodecampeonatos

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.gerenciamentodecampeonatos.network.Pessoa
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

class SignInActivity : AppCompatActivity() {


    private lateinit var etNome: EditText

    private lateinit var etDataNascimento: EditText

    private lateinit var etCpf: EditText

    private lateinit var etEmailCad: EditText

    private lateinit var etEmailConf: EditText

    private lateinit var etSenhaCad: EditText

    private lateinit var etSenhaConf: EditText

    private lateinit var btVoltar: TextView

    private lateinit var btCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        etNome = findViewById(R.id.etNome)

        etDataNascimento = findViewById(R.id.etDataNascimento)
        etCpf = findViewById(R.id.etCpf)

        aplicarMascaraCPF(etCpf)
        aplicarMascaraData(etDataNascimento)

        etEmailCad = findViewById(R.id.etEmailCad)
        etEmailConf = findViewById(R.id.etEmailConf)

        etSenhaCad = findViewById(R.id.etSenhaCad)
        etSenhaConf = findViewById(R.id.etSenhaConf)

        btVoltar = findViewById(R.id.btVoltar)
        btCadastrar = findViewById(R.id.btCadastrar)

        btVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btCadastrar.setOnClickListener {

            val nome = etNome.text.toString().trim()
            val email = etEmailCad.text.toString().trim()
            val emailConf = etEmailConf.text.toString().trim()

            val dataNascimento = etDataNascimento.text.toString()
            val cpf = etCpf.text.toString()

            val senha = etSenhaCad.text.toString()
            val senhaConf = etSenhaConf.text.toString()

            // Verifica se todos os campos foram preenchidos
            if (nome.isEmpty() || email.isEmpty() || emailConf.isEmpty() ||
                senha.isEmpty() || senhaConf.isEmpty() ||
                dataNascimento.isEmpty() || cpf.isEmpty()
            ) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Valida o CPF
            if (!validarCPF(cpf)) {
                etCpf.error = "Digite um CPF válido"
                etCpf.requestFocus()
                return@setOnClickListener
            }

            var dataPraApi = ""
// Valida a data de nascimento
            if (validarDataNascimento(dataNascimento)) {
                dataPraApi = formatarDataParaApi(dataNascimento)
            } else {
                etDataNascimento.error = "Digite uma data válida"
                etDataNascimento.requestFocus()
                return@setOnClickListener
            }


            // Valida o formato do email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailCad.error = "Digite um e-mail válido"
                etEmailCad.requestFocus()
                return@setOnClickListener
            }

            // Verifica se os emails são iguais
            if (email != emailConf) {
                etEmailConf.error = "Os e-mails não correspondem"
                etEmailConf.requestFocus()
                return@setOnClickListener
            }

            // Verifica se as senhas são iguais
            if (senha != senhaConf) {
                etSenhaConf.error = "As senhas não correspondem"
                etSenhaConf.requestFocus()
                return@setOnClickListener
            }

            val cpfLimpo = cpf.filter { it.isDigit() }


            val novaPessoa = Pessoa(
                nome = nome,
                email = email,
                senha = senha,
                cpf = cpfLimpo.toLong(),
                data_nascimento = dataPraApi
            )

            btCadastrar.isEnabled = false
            btCadastrar.text = "Cadastrando..."

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.cadastrarPessoa(novaPessoa)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SignInActivity,
                            "Cadastro realizado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()

                        // Se todas as validações passarem, executa o Intent
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.putExtra("NOME_USUARIO", nome)
                        startActivity(intent)
                    } else {
                        // Se a API recusar (ex: e-mail já existe)
                        val erroMsg = response.errorBody()?.string()
                        android.util.Log.e("DEBUG_CADASTRO", "Erro retornado pela API: $erroMsg")
                        Toast.makeText(
                            this@SignInActivity,
                            "Erro: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@SignInActivity,
                        "Erro de conexão com o servidor: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    // Reativa o botão independente do resultado
                    btCadastrar.isEnabled = true
                    btCadastrar.text = "Cadastrar"
                }
            }


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }



        private fun validarDataNascimento(data: String): Boolean {
            return try {
                val formatador = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT)

                val nascimento = LocalDate.parse(data, formatador)
                val hoje = LocalDate.now()

                // Impede datas futuras
                !nascimento.isAfter(hoje)

            } catch (e: Exception) {
                false
            }
        }
    }

        private fun formatarDataParaApi(data: String): String {
            return try {
                val formatadorEntrada = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                val formatadorSaida = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val dataObjeto = LocalDate.parse(data, formatadorEntrada)
                dataObjeto.format(formatadorSaida) // Retorna "2006-12-30"
            } catch (e: Exception) {
                ""
            }
        }

        private fun validarCPF(cpf: String): Boolean {

            val numeros = cpf.filter { it.isDigit() }

            // Verifica se possui 11 dígitos
            if (numeros.length != 11) return false

            // Impede sequências repetidas, como 111.111.111-11
            if (numeros.all { it == numeros[0] }) return false

            val digitos = numeros.map { it.digitToInt() }

            // Calcula o primeiro dígito verificador
            var soma = 0

            for (i in 0 until 9) {
                soma += digitos[i] * (10 - i)
            }

            var resto = (soma * 10) % 11
            if (resto == 10) resto = 0

            if (resto != digitos[9]) return false

            // Calcula o segundo dígito verificador
            soma = 0

            for (i in 0 until 10) {
                soma += digitos[i] * (11 - i)
            }

            resto = (soma * 10) % 11
            if (resto == 10) resto = 0

            return resto == digitos[10]
        }


        private fun aplicarMascaraCPF(editText: EditText) {
            editText.addTextChangedListener(object : android.text.TextWatcher {

                private var atualizando = false

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                    if (atualizando) return

                    atualizando = true

                    val numeros = s.toString()
                        .filter { it.isDigit() }
                        .take(11)

                    val formatado = buildString {
                        numeros.forEachIndexed { index, char ->
                            if (index == 3 || index == 6) append(".")
                            if (index == 9) append("-")
                            append(char)
                        }
                    }

                    editText.setText(formatado)
                    editText.setSelection(formatado.length)

                    atualizando = false
                }
            })
        }

        private fun aplicarMascaraData(editText: EditText) {
            editText.addTextChangedListener(object : android.text.TextWatcher {

                private var atualizando = false

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                    if (atualizando) return

                    atualizando = true

                    val numeros = s.toString()
                        .filter { it.isDigit() }
                        .take(8)

                    val formatado = buildString {
                        numeros.forEachIndexed { index, char ->
                            if (index == 2 || index == 4) append("/")
                            append(char)
                        }
                    }

                    editText.setText(formatado)
                    editText.setSelection(formatado.length)

                    atualizando = false
                }
            })

        }

