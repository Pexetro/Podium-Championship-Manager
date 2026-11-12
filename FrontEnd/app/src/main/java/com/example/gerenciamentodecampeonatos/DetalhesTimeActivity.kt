package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.*
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class DetalhesTimeActivity : AppCompatActivity() {

    private lateinit var rvAtletas: RecyclerView
    private lateinit var tvSemAtletas: TextView
    private lateinit var fabAddAtleta: FloatingActionButton
    private lateinit var atletaAdapter: AtletaAdapter
    private val listaAtletas = mutableListOf<AtletaTime>()

    private var idTime: Long = 0L
    private var isAdministrador = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_time)

        // Recebe os dados da tela anterior
        idTime = intent.getLongExtra("ID_TIME", 0L)
        val nomeTime = intent.getStringExtra("NOME_TIME")
        findViewById<TextView>(R.id.tvNomeTimeDetalhe).text = nomeTime

        rvAtletas = findViewById(R.id.rvAtletas)
        tvSemAtletas = findViewById(R.id.tvSemAtletas)
        fabAddAtleta = findViewById(R.id.fabAddAtleta)

        // 1. Configura o RecyclerView APENAS UMA VEZ AQUI
        atletaAdapter = AtletaAdapter(listaAtletas, isAdministrador) { atletaTime ->
            confirmarRemocaoAtleta(atletaTime)
        }
        rvAtletas.layoutManager = LinearLayoutManager(this)
        rvAtletas.adapter = atletaAdapter

        // 2. Ação do botão de adicionar atleta (+)
        fabAddAtleta.setOnClickListener {
            abrirDialogoDeSelecaoDePessoa()
        }

        // 3. Verifica a permissão e carrega os atletas
        verificarPermissaoEcarregar()
    }

    private fun verificarPermissaoEcarregar() {
        val idUsuarioLogado = SessionManager(this).obterIdUsuario()

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.buscarTimePorId(idTime)

                if (response.isSuccessful && response.body() != null) {
                    val time = response.body()!!
                    // Verifica se é admin
                    isAdministrador = time.administradores?.any { it.id_pessoa == idUsuarioLogado } ?: false
                    // Mostra/esconde o botão "+"
                    fabAddAtleta.visibility = if (isAdministrador) View.VISIBLE else View.GONE

                    // ATUALIZA O ADAPTER COM A NOVA PERMISSÃO
                    atletaAdapter = AtletaAdapter(listaAtletas, isAdministrador) { atletaTime ->
                        confirmarRemocaoAtleta(atletaTime)
                    }
                    rvAtletas.adapter = atletaAdapter
                }
            } catch (e: Exception) {
                // Se der erro, considera que não é admin por segurança
                isAdministrador = false
            }

            // Depois de verificar, carrega a lista
            carregarAtletasDoTime()
        }
    }

    // --- CARREGAR ATLETAS ---
    private fun carregarAtletasDoTime() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarAtletasDoTime(idTime)

                if (response.isSuccessful && response.body() != null) {
                    listaAtletas.clear()
                    listaAtletas.addAll(response.body()!!)

                    if (listaAtletas.isEmpty()) {
                        rvAtletas.visibility = View.GONE
                        tvSemAtletas.visibility = View.VISIBLE
                    } else {
                        rvAtletas.visibility = View.VISIBLE
                        tvSemAtletas.visibility = View.GONE
                    }

                    // SÓ NOTIFICA O ADAPTER QUE JÁ EXISTE, NÃO CRIA UM NOVO!
                    atletaAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesTimeActivity, "Erro ao buscar elenco.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- ADICIONAR ATLETA (DIALOG COM AUTOCOMPLETE) ---
    private fun abrirDialogoDeSelecaoDePessoa() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarTodasPessoas()

                if (response.isSuccessful && response.body() != null) {
                    val listaPessoas = response.body()!!

                    if (listaPessoas.isEmpty()) {
                        Toast.makeText(this@DetalhesTimeActivity, "Nenhum usuário cadastrado.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val nomesPessoas = listaPessoas.map { it.nome }
                    val dialogView = layoutInflater.inflate(R.layout.dialog_buscar_pessoa, null)
                    val autoComplete = dialogView.findViewById<AutoCompleteTextView>(R.id.autoCompletePessoa)

                    val adapterAutoComplete = ArrayAdapter(this@DetalhesTimeActivity, android.R.layout.simple_dropdown_item_1line, nomesPessoas)
                    autoComplete.setAdapter(adapterAutoComplete)
                    autoComplete.threshold = 1

                    val dialog = AlertDialog.Builder(this@DetalhesTimeActivity)
                        .setTitle("Adicionar Atleta")
                        .setView(dialogView)
                        .setPositiveButton("Adicionar", null)
                        .setNegativeButton("Cancelar", null)
                        .create()

                    dialog.setOnShowListener {
                        val botaoAdicionar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        botaoAdicionar.setOnClickListener {
                            val nomeDigitado = autoComplete.text.toString().trim()
                            val pessoaSelecionada = listaPessoas.find { it.nome.equals(nomeDigitado, ignoreCase = true) }

                            if (pessoaSelecionada != null) {
                                dialog.dismiss()
                                vincularAtletaAoTempo(pessoaSelecionada)
                            } else {
                                autoComplete.error = "Selecione um nome válido"
                            }
                        }
                    }
                    dialog.show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesTimeActivity, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- VINCULAR NA API ---
    private fun vincularAtletaAoTempo(pessoa: Pessoa) {
        val idUsuario = SessionManager(this).obterIdUsuario()

        lifecycleScope.launch {
            try {
                val novoAtleta = Atleta(pessoa = pessoa)
                val responseAtleta = RetrofitClient.apiService.criarAtleta(novoAtleta)

                if (responseAtleta.isSuccessful) {
                    val idAtleta = responseAtleta.body()?.id_atleta

                    if (idAtleta != null) {
                        val responseVinculo = RetrofitClient.apiService.adicionarAtletaAoTime(
                            timeId = idTime,
                            atletaId = idAtleta,
                            usuarioSolicitanteId = idUsuario
                        )

                        if (responseVinculo.isSuccessful) {
                            Toast.makeText(this@DetalhesTimeActivity, "${pessoa.nome} adicionado!", Toast.LENGTH_SHORT).show()
                            carregarAtletasDoTime()
                        } else {
                            Toast.makeText(this@DetalhesTimeActivity, "Sem permissão.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesTimeActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- REMOVER ATLETA ---
    private fun confirmarRemocaoAtleta(atletaTime: AtletaTime) {
        AlertDialog.Builder(this)
            .setTitle("Remover Atleta")
            .setMessage("Deseja realmente remover este atleta?")
            .setPositiveButton("Sim") { _, _ -> removerAtleta(atletaTime) }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun removerAtleta(atletaTime: AtletaTime) {
        val idUsuario = SessionManager(this).obterIdUsuario()
        val idVinculo = atletaTime.id ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.removerAtleta(idVinculo, idUsuario)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesTimeActivity, "Atleta removido.", Toast.LENGTH_SHORT).show()
                    carregarAtletasDoTime()
                } else {
                    Toast.makeText(this@DetalhesTimeActivity, "Sem permissão.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesTimeActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}