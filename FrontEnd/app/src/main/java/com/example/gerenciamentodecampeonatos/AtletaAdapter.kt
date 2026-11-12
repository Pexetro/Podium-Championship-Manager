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
    private val onRemoverClick: (AtletaTime) -> Unit
) : RecyclerView.Adapter<AtletaAdapter.AtletaViewHolder>() {

    class AtletaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.tvNomeAtletaItem)
        val btnRemover: ImageView = view.findViewById(R.id.ivRemoverAtleta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtletaViewHolder {
        // Aqui ele infla o item_atleta.xml que acabamos de criar
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_atleta, parent, false)
        return AtletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtletaViewHolder, position: Int) {
        val atletaTime = lista[position]

        // NAVEGANDO PELO OBJETO: AtletaTime -> Atleta -> Pessoa -> Nome
        val nomeDoAtleta = atletaTime.atleta?.pessoa?.nome ?: "Atleta sem nome"
        holder.nome.text = nomeDoAtleta

        // Configura o botão de remover
        holder.btnRemover.setOnClickListener { onRemoverClick(atletaTime) }
    }

    override fun getItemCount() = lista.size
}