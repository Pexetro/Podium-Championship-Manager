package com.example.gerenciamentodecampeonatos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TimesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_times)

        val fab = findViewById<FloatingActionButton>(R.id.fabAddTime)
        fab.setOnClickListener {
            Toast.makeText(this, "Abrir tela de criação de time...", Toast.LENGTH_SHORT).show()
        }

        // TODO: Configurar o RecyclerView e buscar dados da API (GET /api/times)
    }
}