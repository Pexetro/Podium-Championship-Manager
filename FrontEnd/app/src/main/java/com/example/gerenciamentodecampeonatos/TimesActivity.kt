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
    private val listaTimes = mutableListOf<Time>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_times) // Crie este XML baseado no de campeonatos

        rvTimes = findViewById(R.id.rvTimes) // Adicione os IDs equivalentes no XML
        tvSemTimes = findViewById(R.id.tvSemTimes)
        fabAddTime = findViewById(R.id.fabAddTime)

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
                val response = RetrofitClient.apiService.listarTimes() // @GET("times")
                if (response.isSuccessful && response.body() != null) {
                    listaTimes.clear()

                    listaTimes.addAll(response.body()!!)

                    if (listaTimes.isEmpty()) {
                        rvTimes.visibility = View.GONE
                        tvSemTimes.visibility = View.VISIBLE
                    } else {
                        rvTimes.visibility = View.VISIBLE
                        tvSemTimes.visibility = View.GONE
                    }
                    timeAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TimesActivity, "Erro ao carregar times", Toast.LENGTH_SHORT).show()
            }
        }
    }
}