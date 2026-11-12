package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Jogo

class JogoAdapter(private val lista: List<Jogo>) : RecyclerView.Adapter<JogoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mandante: TextView = view.findViewById(R.id.tvTimeMandante)
        val visitante: TextView = view.findViewById(R.id.tvTimeVisitante)
        val placar: TextView = view.findViewById(R.id.tvPlacar)
        val data: TextView = view.findViewById(R.id.tvDataJogo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = lista[position]
        holder.mandante.text = jogo.timeMandante?.nome_time ?: "Time A"
        holder.visitante.text = jogo.timeVisitante?.nome_time ?: "Time B"
        holder.placar.text = "${jogo.pontuacao?.golsMandante ?: 0} - ${jogo.pontuacao?.golsVisitante ?: 0}"
        holder.data.text = jogo.dataHora ?: "Data a definir"
    }
    override fun getItemCount() = lista.size
}