package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.Jogo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JogoAdapter(
    private val lista: List<Jogo>,
    private val onJogoClick: (Jogo) -> Unit = {}
) : RecyclerView.Adapter<JogoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mandante: TextView = view.findViewById(R.id.tvTimeMandante)
        val visitante: TextView = view.findViewById(R.id.tvTimeVisitante)
        val placar: TextView = view.findViewById(R.id.tvPlacar)
        val placarSets: TextView = view.findViewById(R.id.tvPlacarSets) // Novo
        val data: TextView = view.findViewById(R.id.tvDataJogo)
        val fase: TextView = view.findViewById(R.id.tvFaseJogo)
        val tempoAtual: TextView = view.findViewById(R.id.tvTempoAtual)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jogo = lista[position]

        // Nomes
        holder.mandante.text = jogo.timeMandante?.nome_time ?: "A definir"
        holder.visitante.text = jogo.timeVisitante?.nome_time ?: "A definir"

        // --- LÓGICA DO PLACAR PRINCIPAL (Gols) ---
        // Lê diretamente os valores. Se for nulo, assume 0.
        val golsM = jogo.pontuacao?.golsMandante ?: 0
        val golsV = jogo.pontuacao?.golsVisitante ?: 0

        holder.placar.text = "$golsM - $golsV"

        // --- LÓGICA DO PLACAR DE SETS ---
        val setsM = jogo.pontuacao?.setsMandante
        val setsV = jogo.pontuacao?.setsVisitante

        if (setsM != null && setsV != null) {
            holder.placarSets.text = "($setsM - $setsV set)"
            holder.placarSets.visibility = View.VISIBLE
        } else {
            holder.placarSets.visibility = View.GONE
        }

        // Fase
        holder.fase.text = if (!jogo.faseRodada.isNullOrEmpty()) jogo.faseRodada else "Fase de Grupos"

        // Data
        val dataOriginal = jogo.dataHora
        if (!dataOriginal.isNullOrEmpty()) {
            try {
                val dataParseada = LocalDateTime.parse(dataOriginal)
                val formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                holder.data.text = dataParseada.format(formatador)
            } catch (e: Exception) {
                holder.data.text = dataOriginal
            }
        } else {
            holder.data.text = "Data a definir"
        }

        // Tempo Atual
        if (jogo.status == "Finalizado") {
            holder.tempoAtual.text = "Finalizado"
            holder.tempoAtual.visibility = View.VISIBLE
            holder.tempoAtual.setTextColor(holder.itemView.context.getColor(R.color.primary_green))
        } else if (!jogo.pontuacao?.tempoAtual.isNullOrEmpty()) {
            holder.tempoAtual.text = jogo.pontuacao?.tempoAtual
            holder.tempoAtual.visibility = View.VISIBLE
            holder.tempoAtual.setTextColor(holder.itemView.context.getColor(R.color.text_dark))
        } else {
            holder.tempoAtual.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onJogoClick(jogo) }
    }

    override fun getItemCount() = lista.size
}