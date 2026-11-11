
package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CampeonatoAdapter(
    private val listaCampeonatos: List<Campeonato>,
    private val onCampeonatoClick: (Campeonato) -> Unit
) : RecyclerView.Adapter<CampeonatoAdapter.CampeonatoViewHolder>() {

    class CampeonatoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNome: TextView =
            itemView.findViewById(R.id.tvNomeCampeonato)

        val tvModalidade: TextView =
            itemView.findViewById(R.id.tvModalidadeCampeonato)

        val tvData: TextView =
            itemView.findViewById(R.id.tvDataCampeonato)

        val tvQuantidadeTimes: TextView =
            itemView.findViewById(R.id.tvQuantidadeTimes)
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

        holder.tvNome.text = campeonato.nome
        holder.tvModalidade.text = "Modalidade: ${campeonato.modalidade}"
        holder.tvData.text = "Início: ${campeonato.dataInicio}"
        holder.tvQuantidadeTimes.text =
            "Times participantes: ${campeonato.quantidadeTimes}"

        holder.itemView.setOnClickListener {
            onCampeonatoClick(campeonato)
        }
    }

    override fun getItemCount(): Int {
        return listaCampeonatos.size
    }
}
