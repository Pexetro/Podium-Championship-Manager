package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName

data class Campeonato (
    @SerializedName("idCampeonato") val id_campeonato: Long? = null,
    @SerializedName("nomeCampeonato") val nome_campeonato: String,
    @SerializedName("modalidade") val modalidade: String,
    @SerializedName("dataInicio") val data_inicio: String,
    @SerializedName("dataFim") val data_fim: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("criador") val criador: Pessoa? = null
)


