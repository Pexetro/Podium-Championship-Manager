package com.example.gerenciamentodecampeonatos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.network.Campeonato
import com.example.gerenciamentodecampeonatos.network.Jogo
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.network.Time
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.util.Calendar

class CriarJogoActivity : AppCompatActivity() {
    private var idCampeonato: Long = 0L
    private var listaTimesInscritos = mutableListOf<Time>()
    private var dataHoraFormatada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_jogo)

        idCampeonato = intent.getLongExtra("ID_CAMP", 0L)

        val autoMandante = findViewById<AutoCompleteTextView>(R.id.autoTimeMandante)
        val autoVisitante = findViewById<AutoCompleteTextView>(R.id.autoTimeVisitante)
        val etFase = findViewById<EditText>(R.id.etFaseRodada)
        val btDataHora = findViewById<MaterialButton>(R.id.btDataHora)
        val btSalvar = findViewById<Button>(R.id.btSalvarJogo)

        // Carrega os times e configura o AutoComplete
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarTimesDoCampeonato(idCampeonato)
                if (response.isSuccessful && response.body() != null) {
                    // Extrai apenas os objetos Time da lista de inscrições
                    listaTimesInscritos = response.body()!!.mapNotNull { it.time }.toMutableList()

                    if (listaTimesInscritos.isEmpty()) {
                        Toast.makeText(
                            this@CriarJogoActivity,
                            "Nenhum time inscrito neste campeonato.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val nomes = listaTimesInscritos.map { it.nome_time }
                        val adapter = ArrayAdapter(
                            this@CriarJogoActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            nomes
                        )

                        autoMandante.setAdapter(adapter)
                        autoVisitante.setAdapter(adapter)

                        autoMandante.threshold = 1
                        autoVisitante.threshold = 1
                    }
                } else {
                    Toast.makeText(
                        this@CriarJogoActivity,
                        "Erro ao buscar times do campeonato.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CriarJogoActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }

        btDataHora.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, a, m, d ->
                TimePickerDialog(this, { _, h, min ->
                    dataHoraFormatada =
                        String.format("%04d-%02d-%02dT%02d:%02d:00", a, m + 1, d, h, min)
                    btDataHora.text = dataHoraFormatada
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btSalvar.setOnClickListener {
            val nomeMandante = autoMandante.text.toString().trim()
            val nomeVisitante = autoVisitante.text.toString().trim()

            if (nomeMandante.isEmpty() || nomeVisitante.isEmpty() || dataHoraFormatada.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pega o objeto Time real comparando os nomes
            val timeMandante = listaTimesInscritos.find { it.nome_time == nomeMandante }
            val timeVisitante = listaTimesInscritos.find { it.nome_time == nomeVisitante }

            if (timeMandante == null || timeVisitante == null) {
                Toast.makeText(this, "Selecione times válidos da lista!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (timeMandante.id_time == timeVisitante.id_time) {
                Toast.makeText(this, "Os times não podem ser iguais!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novoJogo = Jogo(
                campeonato = Campeonato(id_campeonato = idCampeonato),
                timeMandante = timeMandante,
                timeVisitante = timeVisitante,
                dataHora = dataHoraFormatada,
                faseRodada = etFase.text.toString(),
                status = "Agendado"
            )

            // Envia para a API
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.criarJogo(novoJogo)
                    if (response.isSuccessful) {
                        Toast.makeText(this@CriarJogoActivity, "Jogo agendado!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@CriarJogoActivity,
                            "Erro: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CriarJogoActivity, "Erro de conexão.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}