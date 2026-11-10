
package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CriarTimeActivity : AppCompatActivity() {

    private lateinit var etNomeTime: EditText
    private lateinit var etQuantidadeJogadores: EditText
    private lateinit var btGerarJogadores: Button
    private lateinit var btSalvarTime: Button
    private lateinit var containerJogadores: LinearLayout

    // Lista que armazenará os campos de cada jogador
    private val camposJogadores = mutableListOf<JogadorCampos>()

    data class JogadorCampos(
        val nome: EditText,
        val posicao: EditText,
        val numeroCamisa: EditText
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_criar_time)

        etNomeTime = findViewById(R.id.etNomeTime)
        etQuantidadeJogadores = findViewById(R.id.etQuantidadeJogadores)
        btGerarJogadores = findViewById(R.id.btGerarJogadores)
        btSalvarTime = findViewById(R.id.btSalvarTime)
        containerJogadores = findViewById(R.id.containerJogadores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Gerar campos dos jogadores
        btGerarJogadores.setOnClickListener {

            val quantidade = etQuantidadeJogadores.text.toString().toIntOrNull()

            if (quantidade == null || quantidade <= 0) {
                etQuantidadeJogadores.error = "Informe uma quantidade válida"
                etQuantidadeJogadores.requestFocus()
                return@setOnClickListener
            }

            if (quantidade > 30) {
                etQuantidadeJogadores.error = "O limite é de 30 jogadores"
                return@setOnClickListener
            }

            gerarCamposJogadores(quantidade)
        }

        // Salvar time
        btSalvarTime.setOnClickListener {

            val nomeTime = etNomeTime.text.toString().trim()

            if (nomeTime.isEmpty()) {
                etNomeTime.error = "Informe o nome do time"
                etNomeTime.requestFocus()
                return@setOnClickListener
            }

            if (camposJogadores.isEmpty()) {
                Toast.makeText(
                    this,
                    "Gere os campos dos jogadores primeiro!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            for ((index, jogador) in camposJogadores.withIndex()) {

                val nome = jogador.nome.text.toString().trim()
                val posicao = jogador.posicao.text.toString().trim()
                val camisa = jogador.numeroCamisa.text.toString().trim()

                if (nome.isEmpty()) {
                    jogador.nome.error = "Informe o nome"
                    jogador.nome.requestFocus()
                    return@setOnClickListener
                }

                if (posicao.isEmpty()) {
                    jogador.posicao.error = "Informe a posição"
                    jogador.posicao.requestFocus()
                    return@setOnClickListener
                }

                if (camisa.isEmpty()) {
                    jogador.numeroCamisa.error = "Informe o número"
                    jogador.numeroCamisa.requestFocus()
                    return@setOnClickListener
                }
            }

            Toast.makeText(
                this,
                "Time $nomeTime validado com sucesso!",
                Toast.LENGTH_LONG
            ).show()

            // Posteriormente, podemos salvar os dados no banco AWS.
        }
    }

    private fun gerarCamposJogadores(quantidade: Int) {

        // Limpa os campos anteriores
        containerJogadores.removeAllViews()
        camposJogadores.clear()

        for (i in 1..quantidade) {

            // Título do jogador
            val titulo = TextView(this).apply {
                text = "Jogador $i"
                textSize = 18f
                setTextColor(android.graphics.Color.BLACK)
                setPadding(0, 20, 0, 10)
            }

            // Campo nome
            val etNome = EditText(this).apply {
                hint = "Nome do jogador"
                inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }

            // Campo posição
            val etPosicao = EditText(this).apply {
                hint = "Posição (ex: Goleiro, Atacante)"
                inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }

            // Campo número da camisa
            val etCamisa = EditText(this).apply {
                hint = "Número da camisa"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }

            // Adiciona os campos ao layout
            containerJogadores.addView(titulo)
            containerJogadores.addView(etNome)
            containerJogadores.addView(etPosicao)
            containerJogadores.addView(etCamisa)

            // Guarda os campos na lista
            camposJogadores.add(
                JogadorCampos(etNome, etPosicao, etCamisa)
            )
        }

        Toast.makeText(
            this,
            "$quantidade jogadores adicionados!",
            Toast.LENGTH_SHORT
        ).show()
    }
}
