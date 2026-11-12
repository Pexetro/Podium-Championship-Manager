package com.example.gerenciamentodecampeonatos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gerenciamentodecampeonatos.utils.SessionManager
import com.google.android.material.card.MaterialCardView

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)

        // 1. Pega o nome do usuário que veio da tela de Login (se enviou via Intent)
        // (Se não enviou, pode deixar fixo ou buscar no SharedPreferences)
        val nomeUsuario = intent.getStringExtra("NOME_USUARIO") ?: "Atleta"
        val tvBoasVindas = findViewById<TextView>(R.id.tvBoasVindas)
        tvBoasVindas.text = "Olá, $nomeUsuario!"

        // 2. Ação do botão de Perfil
        val ivPerfil = findViewById<ImageView>(R.id.ivPerfil)
        ivPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        // 3. Ação do Card de Campeonatos
        val cardCampeonatos = findViewById<MaterialCardView>(R.id.cardCampeonatos)
        cardCampeonatos.setOnClickListener {
            val intent = Intent(this, CampeonatosActivity::class.java)
            startActivity(intent)
        }

        // 4. Ação do Card de Times
        val cardTimes = findViewById<MaterialCardView>(R.id.cardTimes)
        cardTimes.setOnClickListener {
            val intent = Intent(this, TimesActivity::class.java)
            startActivity(intent)
        }
    }
}