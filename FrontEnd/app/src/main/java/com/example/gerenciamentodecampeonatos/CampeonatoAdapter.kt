package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Campeonato // IMPORT CORRETO AQUI

class CampeonatoAdapter(
    private val listaCampeonatos: List<Campeonato>,
    private val onCampeonatoClick: (Campeonato) -> Unit
) : RecyclerView.Adapter<CampeonatoAdapter.CampeonatoViewHolder>() {

    class CampeonatoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNome: TextView = itemView.findViewById(R.id.tvNomeCampeonato)
        val tvModalidade: TextView = itemView.findViewById(R.id.tvModalidadeCampeonato)
        val tvData: TextView = itemView.findViewById(R.id.tvDataCampeonato)
        val tvQuantidadeTimes: TextView = itemView.findViewById(R.id.tvQuantidadeTimes)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CampeonatoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campeonato, parent, false)

        return CampeonatoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CampeonatoViewHolder,
        position: Int
    ) {
        val campeonato = listaCampeonatos[position]

        // Ajustado para ler os campos da API
        holder.tvNome.text = campeonato.nome_campeonato
        holder.tvModalidade.text = "Modalidade: ${campeonato.modalidade}"
        holder.tvData.text = "Início: ${campeonato.data_inicio}"

        // Como não temos esse dado na API ainda, deixamos fixo
        holder.tvQuantidadeTimes.text = "Times participantes: A definir"

        holder.itemView.setOnClickListener {
            onCampeonatoClick(campeonato)
        }
    }

    override fun getItemCount(): Int {
        return listaCampeonatos.size
    }
}