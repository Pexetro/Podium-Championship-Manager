
package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

data class Campeonato(
    val id: String,
    val nome: String,
    val modalidade: String,
    val dataInicio: String,
    val quantidadeTimes: Int
)

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

        carregarCampeonatos()
    }

    private fun inicializarComponentes() {

        rvCampeonatos = findViewById(R.id.rvCampeonatos)

        tvSemCampeonatos = findViewById(R.id.tvSemCampeonatos)

        fabAddCampeonato = findViewById(R.id.fabAddCampeonato)
    }

    private fun configurarRecyclerView() {

        campeonatoAdapter = CampeonatoAdapter(listaCampeonatos) { campeonato ->

            // Futuramente abrirá a tela de detalhes.
            // O ID identifica o campeonato selecionado.

        }

        rvCampeonatos.layoutManager = LinearLayoutManager(this)

        rvCampeonatos.adapter = campeonatoAdapter
    }

    private fun configurarBotoes() {

        fabAddCampeonato.setOnClickListener {

            // Substitua pelo nome real da sua Activity de criação.
            // Exemplo:
            // startActivity(
            //     Intent(this, CriarCampeonatoActivity::class.java)
            // )

        }
    }

    private fun carregarCampeonatos() {

        // Futuramente será realizada a consulta ao Supabase.

        atualizarLista()
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
