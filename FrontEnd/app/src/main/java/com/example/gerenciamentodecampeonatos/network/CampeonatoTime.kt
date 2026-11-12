package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CampeonatoTime(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("campeonato") val campeonato: Campeonato? = null,
    @SerializedName("time") val time: Time? = null,
    @SerializedName("data_inscricao") val dataInscricao: String? = null
): Serializable