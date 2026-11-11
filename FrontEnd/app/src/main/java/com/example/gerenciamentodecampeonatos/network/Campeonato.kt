package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Campeonato (
    @SerializedName("id_campeonato") val id_campeonato: Long? = null,
    @SerializedName("nome_campeonato") val nome_campeonato: String,
    @SerializedName("modalidade") val modalidade: String,
    @SerializedName("data_inicio") val data_inicio: String,
    @SerializedName("data_fim") val data_fim: String,
    @SerializedName("descricao") val descricao: String
    )


