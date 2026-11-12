package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.network.DetalhesPontuacao
import com.example.gerenciamentodecampeonatos.network.Jogo
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetalhesJogoActivity : AppCompatActivity() {

    private lateinit var jogo: Jogo
    private var isAdmin = false

    private lateinit var cardEdicao: MaterialCardView
    private lateinit var btSalvarPlacar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_jogo)

        // Aplica os espaçamentos de segurança (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebe o Jogo
        jogo = intent.getSerializableExtra("OBJ_JOGO") as Jogo

        // 1. Preenche os dados visuais principais
        findViewById<TextView>(R.id.tvFaseJogoDetalhe).text = jogo.faseRodada ?: "Fase de Grupos"
        findViewById<TextView>(R.id.tvTimeMandanteDetalhe).text = jogo.timeMandante?.nome_time ?: "Time A"
        findViewById<TextView>(R.id.tvTimeVisitanteDetalhe).text = jogo.timeVisitante?.nome_time ?: "Time B"

        val golsM = jogo.pontuacao?.golsMandante ?: 0
        val golsV = jogo.pontuacao?.golsVisitante ?: 0
        findViewById<TextView>(R.id.tvPlacarDetalhe).text = "$golsM - $golsV"

        // 2. Preenche os detalhes extras (Sets e Tempo Atual)
        val tvSetsDetalhe = findViewById<TextView>(R.id.tvSetsDetalhe)
        val setsM = jogo.pontuacao?.setsMandante
        val setsV = jogo.pontuacao?.setsVisitante

        if (setsM != null && setsV != null) {
            tvSetsDetalhe.text = "($setsM - $setsV SET)"
            tvSetsDetalhe.visibility = View.VISIBLE
        } else {
            tvSetsDetalhe.visibility = View.GONE
        }

        val tvTempoDetalhe = findViewById<TextView>(R.id.tvTempoDetalhe)
        if (jogo.status == "Finalizado") {
            tvTempoDetalhe.text = "FINALIZADO"
            tvTempoDetalhe.setTextColor(getColor(R.color.primary_green))
            tvTempoDetalhe.visibility = View.VISIBLE
        } else if (!jogo.pontuacao?.tempoAtual.isNullOrEmpty()) {
            tvTempoDetalhe.text = jogo.pontuacao?.tempoAtual
            tvTempoDetalhe.setTextColor(getColor(R.color.text_dark))
            tvTempoDetalhe.visibility = View.VISIBLE
        } else {
            tvTempoDetalhe.visibility = View.GONE
        }

        // 3. Preenche a Data
        try {
            val dataParseada = LocalDateTime.parse(jogo.dataHora)
            val formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
            findViewById<TextView>(R.id.tvDataHoraDetalhe).text = dataParseada.format(formatador)
        } catch (e: Exception) {
            findViewById<TextView>(R.id.tvDataHoraDetalhe).text = jogo.dataHora ?: "Data a definir"
        }

        // 4. Componentes de Edição
        cardEdicao = findViewById(R.id.cardEdicao)
        btSalvarPlacar = findViewById(R.id.btSalvarPlacar)

        val etGolsM = findViewById<EditText>(R.id.etGolsMandanteDetalhe)
        val etGolsV = findViewById<EditText>(R.id.etGolsVisitanteDetalhe)
        val etSetsM = findViewById<EditText>(R.id.etSetsMandante)
        val etSetsV = findViewById<EditText>(R.id.etSetsVisitante)
        val etTempo = findViewById<EditText>(R.id.etTempoAtual)
        val tvTituloTempo = findViewById<TextView>(R.id.tvTituloTempo)
        val cbFinalizar = findViewById<CheckBox>(R.id.cbFinalizarDetalhe)

        // Preenche os campos de edição
        if (jogo.pontuacao != null) {
            etGolsM.setText(golsM.toString())
            etGolsV.setText(golsV.toString())
            if (jogo.pontuacao?.setsMandante != null) etSetsM.setText(jogo.pontuacao?.setsMandante.toString())
            if (jogo.pontuacao?.setsVisitante != null) etSetsV.setText(jogo.pontuacao?.setsVisitante.toString())
            if (jogo.pontuacao?.tempoAtual != null) etTempo.setText(jogo.pontuacao?.tempoAtual)
        }
        cbFinalizar.isChecked = jogo.status == "Finalizado"

        // Lógica para esconder/mostrar o campo de Tempo ao marcar "Finalizado"
        fun atualizarVisibilidadeTempo(isFinalizado: Boolean) {
            if (isFinalizado) {
                etTempo.visibility = View.GONE
                tvTituloTempo.visibility = View.GONE
            } else {
                etTempo.visibility = View.VISIBLE
                tvTituloTempo.visibility = View.VISIBLE
            }
        }

        atualizarVisibilidadeTempo(cbFinalizar.isChecked)
        cbFinalizar.setOnCheckedChangeListener { _, isChecked ->
            atualizarVisibilidadeTempo(isChecked)
        }

        // 5. Ação do botão Salvar
        btSalvarPlacar.setOnClickListener {
            val valorM = etGolsM.text.toString()
            val valorV = etGolsV.text.toString()
            val intM = if (valorM.isNotEmpty()) valorM.toInt() else 0
            val intV = if (valorV.isNotEmpty()) valorV.toInt() else 0

            val valorSetsM = etSetsM.text.toString()
            val valorSetsV = etSetsV.text.toString()
            val setsM = if (valorSetsM.isNotEmpty()) valorSetsM.toInt() else null
            val setsV = if (valorSetsV.isNotEmpty()) valorSetsV.toInt() else null

            val tempoAtual = if (cbFinalizar.isChecked) null else etTempo.text.toString().takeIf { it.isNotEmpty() }
            val novoStatus = if (cbFinalizar.isChecked) "Finalizado" else "Em Andamento"

            val jogoAtualizado = Jogo(
                pontuacao = DetalhesPontuacao(
                    golsMandante = intM,
                    golsVisitante = intV,
                    setsMandante = setsM,
                    setsVisitante = setsV,
                    tempoAtual = tempoAtual
                ),
                status = novoStatus
            )

            enviarPlacarParaApi(jogo.id_jogo!!, jogoAtualizado)
        }

        // 6. Verifica permissão de administrador
        verificarPermissaoEdicao()
    }

    private fun verificarPermissaoEdicao() {
        val idUsuarioLogado = SessionManager(this).obterIdUsuario()
        val idCampeonato = jogo.campeonato?.id_campeonato

        if (idCampeonato == null) return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.buscarCampeonatoPorId(idCampeonato)

                if (response.isSuccessful && response.body() != null) {
                    val campeonato = response.body()!!
                    isAdmin = campeonato.administradores?.any { it.id_pessoa == idUsuarioLogado } ?: false

                    if (isAdmin) {
                        cardEdicao.visibility = View.VISIBLE
                        btSalvarPlacar.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                cardEdicao.visibility = View.GONE
                btSalvarPlacar.visibility = View.GONE
            }
        }
    }

    private fun enviarPlacarParaApi(idJogo: Long, dadosPlacar: Jogo) {
        val idUsuario = SessionManager(this).obterIdUsuario()

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.atualizarPlacar(idJogo, dadosPlacar, idUsuario)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesJogoActivity, "Placar atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DetalhesJogoActivity, "Erro: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesJogoActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}