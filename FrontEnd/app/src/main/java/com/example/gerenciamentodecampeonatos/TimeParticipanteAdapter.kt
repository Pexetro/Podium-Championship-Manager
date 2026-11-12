package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Time

class TimeParticipanteAdapter(
    private val listaTimes: List<Time>,
    private val onTimeClick: (Time) -> Unit // <--- Adicionado aqui
) : RecyclerView.Adapter<TimeParticipanteAdapter.TimeViewHolder>() {

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNomeTime)
        // Se você tiver o TextView de modalidade no item_time.xml, adicione ele aqui também
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = listaTimes[position]
        holder.tvNome.text = time.nome_time ?: "Time sem nome"

        // Adiciona o clique no card inteiro
        holder.itemView.setOnClickListener {
            onTimeClick(time)
        }
    }

    override fun getItemCount() = listaTimes.size
}