package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Time

class TimeAdapter(
    private val listaTimes: List<Time>,
    private val onTimeClick: (Time) -> Unit
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNomeTime)
        val tvModalidade: TextView = itemView.findViewById(R.id.tvModalidadeTime)
        val tvJogadores: TextView = itemView.findViewById(R.id.tvQuantidadeJogadores)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = listaTimes[position]
        holder.tvNome.text = time.nome_time
        holder.tvModalidade.text = "Modalidade: A definir" // Ainda não temos modalidade no Time
        holder.tvJogadores.text = "Jogadores: 0" // Ainda não temos contagem

        holder.itemView.setOnClickListener { onTimeClick(time) }
    }

    override fun getItemCount() = listaTimes.size
}