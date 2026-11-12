package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.RetrofitClient
import kotlinx.coroutines.launch

class DetalhesCampActivity : AppCompatActivity() {
    private lateinit var rvJogos: RecyclerView
    private lateinit var jogoAdapter: JogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_camp)

        val idCamp = intent.getLongExtra("ID_CAMP", 0L)
        val nomeCamp = intent.getStringExtra("NOME_CAMP")
        findViewById<TextView>(R.id.tvNomeCampDetalhe).text = nomeCamp

        rvJogos = findViewById(R.id.rvJogos)
        rvJogos.layoutManager = LinearLayoutManager(this)

        // Busca os jogos da API
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listarJogosDoCampeonato(idCamp)
                if (response.isSuccessful && response.body() != null) {
                    jogoAdapter = JogoAdapter(response.body()!!)
                    rvJogos.adapter = jogoAdapter
                }
            } catch (e: Exception) {
                // Ignora erro
            }
        }
    }
}