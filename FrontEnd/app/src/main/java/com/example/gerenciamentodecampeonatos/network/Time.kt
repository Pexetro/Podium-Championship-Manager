package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Time(
    @SerializedName("idTime") val id_time: Long? = null,
    @SerializedName("nomeTime") val nome_time: String?,
    @SerializedName("criador") val criador: Pessoa? = null,
    @SerializedName("administradores") val administradores: List<Pessoa>? = null
): Serializable