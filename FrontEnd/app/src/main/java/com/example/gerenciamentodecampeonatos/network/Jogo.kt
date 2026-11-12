package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Jogo(
    @SerializedName("id_jogo") val id_jogo: Long? = null,
    @SerializedName("campeonato") val campeonato: Campeonato? = null,
    @SerializedName("timeMandante") val timeMandante: Time? = null,
    @SerializedName("timeVisitante") val timeVisitante: Time? = null,
    @SerializedName("dataHora") val dataHora: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("faseRodada") val faseRodada: String? = null,
    @SerializedName("pontuacao") val pontuacao: DetalhesPontuacao? = null
): Serializable

data class DetalhesPontuacao(
    @SerializedName("pontosMandante") val golsMandante: Int? = null,
    @SerializedName("pontosVisitante") val golsVisitante: Int? = null,
    @SerializedName("setsMandante") val setsMandante: Int? = null,
    @SerializedName("setsVisitante") val setsVisitante: Int? = null,
    @SerializedName("tempoAtual") val tempoAtual: String? = null
): Serializable