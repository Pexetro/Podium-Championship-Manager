package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class Hub : AppCompatActivity() {

    private lateinit var tvNome: TextView

    private lateinit var btCriar: Button
    private lateinit var btCriarCamp: Button
    private lateinit var btEntrar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hub)

        btCriar = findViewById(R.id.btCriar)
        btCriarCamp = findViewById(R.id.btCriarCamp)
        btEntrar = findViewById(R.id.btEntrar)

        tvNome = findViewById(R.id.textView6)

        val nome = intent.getStringExtra("NOME_USUARIO")

        tvNome.text = "Bem-vindo, $nome!"

        btCriar.setOnClickListener {
            val intent = Intent(this, CriarTime::class.java)
            startActivity(intent)

        }
        btCriarCamp.setOnClickListener {
            val intent = Intent(this, CriarCamp::class.java)
            startActivity(intent)


        }

        btEntrar.setOnClickListener {
            val intent = Intent(this, EntrarTime::class.java)
            startActivity(intent)


        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}