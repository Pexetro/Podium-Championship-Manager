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
import com.example.gerenciamentodecampeonatos.network.Campeonato
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class CampeonatosActivity : AppCompatActivity() {

    private lateinit var rvCampeonatos: RecyclerView
    private lateinit var tvSemCampeonatos: TextView
    private lateinit var fabAddCampeonato: FloatingActionButton

    private lateinit var campeonatoAdapter: CampeonatoAdapter
    private val listaCampeonatos = mutableListOf<Campeonato>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campeonatos)

        inicializarComponentes()
        configurarRecyclerView()
        configurarBotoes()
    }

    override fun onResume() {
        super.onResume()
        carregarCampeonatos()
    }

    private fun inicializarComponentes() {
        rvCampeonatos = findViewById(R.id.rvCampeonatos)
        tvSemCampeonatos = findViewById(R.id.tvSemCampeonatos)
        fabAddCampeonato = findViewById(R.id.fabAddCampeonato)
    }

    private fun configurarRecyclerView() {
        campeonatoAdapter = CampeonatoAdapter(listaCampeonatos) { campeonato ->
            Toast.makeText(this, "Abrir detalhes de: ${campeonato.nome_campeonato}", Toast.LENGTH_SHORT).show()
        }
        rvCampeonatos.layoutManager = LinearLayoutManager(this)
        rvCampeonatos.adapter = campeonatoAdapter
    }

    private fun configurarBotoes() {
        fabAddCampeonato.setOnClickListener {
            startActivity(Intent(this, CriarCampActivity::class.java))
        }
    }

    private fun carregarCampeonatos() {
        val session = SessionManager(this)
        val idUsuarioLogado = session.obterIdUsuario()

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarCampeonatos()

                if (response.isSuccessful && response.body() != null) {
                    listaCampeonatos.clear()

                    // Filtra para mostrar apenas os do usuário
                    val todos = response.body()!!
                    listaCampeonatos.addAll(response.body()!!)

                    atualizarLista()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CampeonatosActivity, "Erro de conexão com a API.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun atualizarLista() {
        if (listaCampeonatos.isEmpty()) {
            rvCampeonatos.visibility = View.GONE
            tvSemCampeonatos.visibility = View.VISIBLE
        } else {
            rvCampeonatos.visibility = View.VISIBLE
            tvSemCampeonatos.visibility = View.GONE
        }
        campeonatoAdapter.notifyDataSetChanged()
    }
}