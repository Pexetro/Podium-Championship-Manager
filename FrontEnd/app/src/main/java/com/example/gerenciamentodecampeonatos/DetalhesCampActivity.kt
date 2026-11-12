package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Campeonato
import com.example.gerenciamentodecampeonatos.network.CampeonatoTime
import com.example.gerenciamentodecampeonatos.network.Jogo
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.network.Time
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class DetalhesCampActivity : AppCompatActivity() {

    private lateinit var rvJogos: RecyclerView
    private lateinit var tvSemJogos: TextView
    private lateinit var jogoAdapter: JogoAdapter
    private val listaJogos = mutableListOf<Jogo>()

    private lateinit var rvTimesCamp: RecyclerView
    private lateinit var tvSemTimesCamp: TextView
    private val listaTimesInscritos = mutableListOf<Time>()

    private lateinit var fabCriarJogo: FloatingActionButton
    private lateinit var fabInscreverTime: FloatingActionButton
    private lateinit var tabLayout: TabLayout

    private var idCampeonato: Long = 0L
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_camp)

        idCampeonato = intent.getLongExtra("ID_CAMP", 0L)
        val nomeCamp = intent.getStringExtra("NOME_CAMP")
        findViewById<TextView>(R.id.tvNomeCampDetalhe).text = nomeCamp

        rvJogos = findViewById(R.id.rvJogos)
        tvSemJogos = findViewById(R.id.tvSemJogos)
        jogoAdapter = JogoAdapter(listaJogos) { jogoClicado ->
            val intent = Intent(this, DetalhesJogoActivity::class.java)
            intent.putExtra("OBJ_JOGO", jogoClicado)
            startActivity(intent)
        }
        rvJogos.layoutManager = LinearLayoutManager(this)
        rvJogos.adapter = jogoAdapter

        rvTimesCamp = findViewById(R.id.rvTimesCamp)
        tvSemTimesCamp = findViewById(R.id.tvSemTimesCamp)
        rvTimesCamp.layoutManager = LinearLayoutManager(this)

        fabCriarJogo = findViewById(R.id.fabCriarJogo)
        fabInscreverTime = findViewById(R.id.fabInscreverTime)
        tabLayout = findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Esconde os textos de "vazio" antes de trocar
                tvSemJogos.visibility = View.GONE
                tvSemTimesCamp.visibility = View.GONE

                if (tab?.position == 0) {
                    // Aba "Jogos"
                    rvJogos.visibility = View.VISIBLE
                    rvTimesCamp.visibility = View.GONE
                    verificarVazioJogos()
                } else {
                    // Aba "Times"
                    rvJogos.visibility = View.GONE
                    rvTimesCamp.visibility = View.VISIBLE
                    carregarTimesInscritos()
                }
                atualizarBotoesVisiveis()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        fabCriarJogo.setOnClickListener {
            val intent = Intent(this, CriarJogoActivity::class.java)
            intent.putExtra("ID_CAMP", idCampeonato)
            startActivity(intent)
        }

        fabInscreverTime.setOnClickListener {
            abrirDialogoInscreverTime()
        }

        verificarPermissaoEdicao()
        carregarJogos()
    }

    private fun atualizarBotoesVisiveis() {
        if (!isAdmin) {
            fabCriarJogo.visibility = View.GONE
            fabInscreverTime.visibility = View.GONE
            return
        }

        if (tabLayout.selectedTabPosition == 0) {
            fabCriarJogo.visibility = View.VISIBLE
            fabInscreverTime.visibility = View.GONE
        } else {
            fabCriarJogo.visibility = View.GONE
            fabInscreverTime.visibility = View.VISIBLE
        }
    }

    private fun verificarPermissaoEdicao() {
        val idUsuarioLogado = SessionManager(this).obterIdUsuario()

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.buscarCampeonatoPorId(idCampeonato)
                if (response.isSuccessful && response.body() != null) {
                    val campeonato = response.body()!!
                    isAdmin = campeonato.administradores?.any { it.id_pessoa == idUsuarioLogado } ?: false
                    atualizarBotoesVisiveis()
                }
            } catch (e: Exception) {
                isAdmin = false
                atualizarBotoesVisiveis()
            }
        }
    }



    private fun carregarJogos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarJogosDoCampeonato(idCampeonato)
                if (response.isSuccessful && response.body() != null) {
                    listaJogos.clear()
                    listaJogos.addAll(response.body()!!)
                    jogoAdapter.notifyDataSetChanged()
                    verificarVazioJogos()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesCampActivity, "Erro ao buscar jogos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarVazioJogos() {
        tvSemTimesCamp.visibility = View.GONE

        if (listaJogos.isEmpty()) {
            rvJogos.visibility = View.GONE
            tvSemJogos.visibility = View.VISIBLE
        } else {
            rvJogos.visibility = View.VISIBLE
            tvSemJogos.visibility = View.GONE
        }
    }

    private fun carregarTimesInscritos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarTimesDoCampeonato(idCampeonato)
                if (response.isSuccessful && response.body() != null) {
                    listaTimesInscritos.clear()
                    val times = response.body()!!.mapNotNull { it.time }
                    listaTimesInscritos.addAll(times)

                    // Esconde o texto de jogos
                    tvSemJogos.visibility = View.GONE

                    if (listaTimesInscritos.isEmpty()) {
                        rvTimesCamp.visibility = View.GONE
                        tvSemTimesCamp.visibility = View.VISIBLE
                    } else {
                        rvTimesCamp.visibility = View.VISIBLE
                        tvSemTimesCamp.visibility = View.GONE
                        val adapter = TimeParticipanteAdapter(listaTimesInscritos) { timeClicado ->
                            val intent = Intent(this@DetalhesCampActivity, DetalhesTimeActivity::class.java)
                            intent.putExtra("ID_TIME", timeClicado.id_time)
                            intent.putExtra("NOME_TIME", timeClicado.nome_time)
                            startActivity(intent)
                        }
                        rvTimesCamp.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesCampActivity, "Erro ao buscar times.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- FUNÇÃO COM OS LOGS DE DEBUG ---
    private fun abrirDialogoInscreverTime() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarTimes()

                // LOGS
                Log.d("DEBUG_TIME", "Status da API: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val todosOsTimes = response.body()!!
                    Log.d("DEBUG_TIME", "Total de times no banco: ${todosOsTimes.size}")

                    // Ignora times sem nome
                    val timesComNome = todosOsTimes.filter { !it.nome_time.isNullOrBlank() }
                    Log.d("DEBUG_TIME", "Times com nome válido: ${timesComNome.size}")

                    if (timesComNome.isEmpty()) {
                        Toast.makeText(this@DetalhesCampActivity, "Nenhum time válido cadastrado. Crie um time primeiro.", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    val dialogView = layoutInflater.inflate(R.layout.dialog_inscrever_time, null)
                    val autoComplete = dialogView.findViewById<AutoCompleteTextView>(R.id.autoCompleteTime)

                    val nomesTimes = timesComNome.map { it.nome_time!! }
                    Log.d("DEBUG_TIME", "Nomes no AutoComplete: $nomesTimes")

                    val adapter = ArrayAdapter(this@DetalhesCampActivity, android.R.layout.simple_dropdown_item_1line, nomesTimes)
                    autoComplete.setAdapter(adapter)
                    autoComplete.threshold = 1

                    AlertDialog.Builder(this@DetalhesCampActivity)
                        .setTitle("Inscrever Time")
                        .setView(dialogView)
                        .setPositiveButton("Inscrever") { _, _ ->
                            val nomeDigitado = autoComplete.text.toString().trim()
                            // Encontra o time pelo nome (ignorando maiúsculas/minúsculas)
                            val timeSelecionado = timesComNome.find { it.nome_time.equals(nomeDigitado, ignoreCase = true) }

                            if (timeSelecionado != null) {
                                inscreverTime(timeSelecionado)
                            } else {
                                Toast.makeText(this@DetalhesCampActivity, "Selecione um time válido da lista.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_TIME", "Erro ao buscar times: ${e.message}")
                Toast.makeText(this@DetalhesCampActivity, "Erro ao buscar times.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inscreverTime(time: Time) {
        val campeonatoTime = CampeonatoTime(
            campeonato = Campeonato(id_campeonato = idCampeonato),
            time = time
        )
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.inscreverTimeNoCampeonato(campeonatoTime)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesCampActivity, "${time.nome_time} inscrito!", Toast.LENGTH_SHORT).show()
                    if (tabLayout.selectedTabPosition == 1) {
                        carregarTimesInscritos()
                    }
                } else {
                    Toast.makeText(this@DetalhesCampActivity, "Erro: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesCampActivity, "Erro de rede.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}