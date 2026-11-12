package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.example.gerenciamentodecampeonatos.network.Time
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TimesActivity : AppCompatActivity() {

    private lateinit var rvTimes: RecyclerView
    private lateinit var tvSemTimes: TextView
    private lateinit var fabAddTime: FloatingActionButton
    private lateinit var timeAdapter: TimeAdapter

    private val listaTimesOriginal = mutableListOf<Time>()
    private val listaTimesExibidos = mutableListOf<Time>()
    private var isFiltrandoMeusTimes = false
    private val listaTimes = mutableListOf<Time>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_times)

        rvTimes = findViewById(R.id.rvTimes)
        tvSemTimes = findViewById(R.id.tvSemTimes)
        fabAddTime = findViewById(R.id.fabAddTime)

        val btFiltro = findViewById<com.google.android.material.button.MaterialButton>(R.id.btFiltroMeusTimes)
        btFiltro.setOnClickListener {
            isFiltrandoMeusTimes = !isFiltrandoMeusTimes
            aplicarFiltro()
        }

        timeAdapter = TimeAdapter(listaTimes) { time ->
            // Ação ao clicar no time
            val intent = Intent(this, DetalhesTimeActivity::class.java)
            intent.putExtra("ID_TIME", time.id_time)
            intent.putExtra("NOME_TIME", time.nome_time)
            startActivity(intent)
        }
        rvTimes.layoutManager = LinearLayoutManager(this)
        rvTimes.adapter = timeAdapter

        fabAddTime.setOnClickListener {
            startActivity(Intent(this, CriarTimeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        carregarTimes()
    }

    private fun carregarTimes() {
        val idUsuario = SessionManager(this).obterIdUsuario()

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarTimes()
                if (response.isSuccessful && response.body() != null) {
                    listaTimesOriginal.clear()
                    listaTimesOriginal.addAll(response.body()!!)

                    aplicarFiltro() // Chama o filtro logo após carregar
                }
            } catch (e: Exception) {
                Toast.makeText(this@TimesActivity, "Erro ao carregar times", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun aplicarFiltro() {
        val idUsuario = SessionManager(this).obterIdUsuario()
        listaTimesExibidos.clear()

        val btFiltro = findViewById<com.google.android.material.button.MaterialButton>(R.id.btFiltroMeusTimes)

        if (isFiltrandoMeusTimes) {
            // Filtro ATIVO
            val meus = listaTimesOriginal.filter { time ->
                val souAdmin = time.administradores?.any { it.id_pessoa == idUsuario } ?: false
                val souCriador = time.criador?.id_pessoa == idUsuario
                souAdmin || souCriador
            }
            listaTimesExibidos.addAll(meus)

            // Muda a cor para indicar que está ativo
            btFiltro.text = "Apenas Meus" // Ou "Filtro: Meus"
            btFiltro.setBackgroundColor(getColor(R.color.primary_green))
            btFiltro.setTextColor(getColor(android.R.color.white))

        } else {
            // Filtro INATIVO
            listaTimesExibidos.addAll(listaTimesOriginal)

            // Volta para a cor original
            btFiltro.text = "Ver Todos"
            btFiltro.setBackgroundColor(getColor(android.R.color.transparent)) // Ou a cor surface_white
            btFiltro.setTextColor(getColor(R.color.primary_green))
        }

        // Atualiza o Adapter (igual ao que já estava)
        timeAdapter = TimeAdapter(listaTimesExibidos) { time ->
            // ... clique ...
        }
        rvTimes.adapter = timeAdapter

        // Atualiza o texto "vazio"
        if (listaTimesExibidos.isEmpty()) {
            rvTimes.visibility = View.GONE
            tvSemTimes.visibility = View.VISIBLE
            tvSemTimes.text = if (isFiltrandoMeusTimes) "Você não participa de nenhum time." else "Nenhum time cadastrado."
        } else {
            rvTimes.visibility = View.VISIBLE
            tvSemTimes.visibility = View.GONE
        }
    }
}