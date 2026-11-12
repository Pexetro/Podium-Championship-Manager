package com.example.gerenciamentodecampeonatos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gerenciamentodecampeonatos.network.Campeonato
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.util.Calendar

class CriarCampActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var spModalidade: Spinner
    private lateinit var btDataInicio: MaterialButton
    private lateinit var btDataFim: MaterialButton
    private lateinit var btCriar: MaterialButton

    // Variáveis para guardar as datas selecionadas
    private var dataInicioSelecionada = ""
    private var dataFimSelecionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_camp)

        // 1. Linkando os IDs
        etNome = findViewById(R.id.etNomeCamp)
        etDescricao = findViewById(R.id.etDescricao)
        spModalidade = findViewById(R.id.spModalidade)
        btDataInicio = findViewById(R.id.btDataInicio)
        btDataFim = findViewById(R.id.btDataFim)
        btCriar = findViewById(R.id.btCriarCamp)

        // 2. Configurar o Spinner de Modalidades
        val modalidades = arrayOf("Futebol", "Vôlei", "Basquete", "E-sports", "Outro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modalidades)
        spModalidade.adapter = adapter

        // 3. Ações dos botões de data
        btDataInicio.setOnClickListener { abrirCalendario { data ->
            dataInicioSelecionada = data
            btDataInicio.text = data
        }}

        btDataFim.setOnClickListener { abrirCalendario { data ->
            dataFimSelecionada = data
            btDataFim.text = data
        }}

        // 4. Ação do botão Criar
        btCriar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val descricao = etDescricao.text.toString().trim()
            val modalidade = spModalidade.selectedItem.toString()

            // Validação
            if (nome.isEmpty() || descricao.isEmpty() || dataInicioSelecionada.isEmpty() || dataFimSelecionada.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos e datas!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pega o ID do usuário logado
            val session = SessionManager(this)
            val idCriador = session.obterIdUsuario()

            if (idCriador == 0L) {
                Toast.makeText(this, "Sessão expirada. Faça login.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria o objeto
            val novoCampeonato = Campeonato(
                nome_campeonato = nome,
                modalidade = modalidade,
                data_inicio = dataInicioSelecionada,
                data_fim = dataFimSelecionada,
                descricao = descricao
            )

            salvarCampeonato(novoCampeonato, idCriador)
        }
    }

    // Função auxiliar para abrir o calendário
    private fun abrirCalendario(onDataSelecionada: (String) -> Unit) {
        val calendario = Calendar.getInstance()
        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, a, m, d ->
            // Formata para AAAA-MM-DD (padrão da API)
            val mesFormatado = String.format("%02d", m + 1)
            val diaFormatado = String.format("%02d", d)
            val dataFormatada = "$a-$mesFormatado-$diaFormatado"

            onDataSelecionada(dataFormatada)
        }, ano, mes, dia)

        datePicker.show()
    }

    // Função que chama a API
    private fun salvarCampeonato(campeonato: Campeonato, criadorId: Long) {
        btCriar.isEnabled = false
        btCriar.text = "Criando..."

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.criarCampeonato(campeonato, criadorId)

                if (response.isSuccessful) {
                    Toast.makeText(this@CriarCampActivity, "Campeonato criado com sucesso!", Toast.LENGTH_LONG).show()
                    finish() // Fecha a tela e volta para a lista
                } else {
                    val erro = response.errorBody()?.string()
                    Toast.makeText(this@CriarCampActivity, "Erro: $erro", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CriarCampActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                btCriar.isEnabled = true
                btCriar.text = "Criar Campeonato"
            }
        }
    }
}