package com.example.gerenciamentodecampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciamentodecampeonatos.network.AtletaTime

class AtletaAdapter(
    private val lista: List<AtletaTime>,
    private val isAdmin: Boolean, // <--- NOVO PARÂMETRO
    private val onRemoverClick: (AtletaTime) -> Unit
) : RecyclerView.Adapter<AtletaAdapter.AtletaViewHolder>() {

    class AtletaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.tvNomeAtletaItem)
        val btnRemover: ImageView = view.findViewById(R.id.ivRemoverAtleta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_atleta, parent, false)
        return AtletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtletaViewHolder, position: Int) {
        val atletaTime = lista[position]

        // Navegando pelo objeto: AtletaTime -> Atleta -> Pessoa -> Nome
        val nomeDoAtleta = atletaTime.atleta?.pessoa?.nome ?: "Atleta sem nome"
        holder.nome.text = nomeDoAtleta

        // LÓGICA DE VISIBILIDADE DA LIXEIRA
        if (isAdmin) {
            holder.btnRemover.visibility = View.VISIBLE
            holder.btnRemover.setOnClickListener { onRemoverClick(atletaTime) }
        } else {
            holder.btnRemover.visibility = View.GONE
        }
    }

    override fun getItemCount() = lista.size
}