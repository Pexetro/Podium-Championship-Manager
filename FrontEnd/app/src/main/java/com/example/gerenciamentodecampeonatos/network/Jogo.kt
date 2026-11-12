package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName

data class Jogo(
    @SerializedName("id_jogo") val id_jogo: Long? = null,
    @SerializedName("timeMandante") val timeMandante: Time? = null,
    @SerializedName("timeVisitante") val timeVisitante: Time? = null,
    @SerializedName("dataHora") val dataHora: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("faseRodada") val faseRodada: String? = null,
    @SerializedName("pontuacao") val pontuacao: DetalhesPontuacao? = null
)

data class DetalhesPontuacao(
    @SerializedName("golsMandante") val golsMandante: Int? = null,
    @SerializedName("golsVisitante") val golsVisitante: Int? = null,
    @SerializedName("setsMandante") val setsMandante: Int? = null,
    @SerializedName("setsVisitante") val setsVisitante: Int? = null,
    @SerializedName("tempoAtual") val tempoAtual: String? = null
)