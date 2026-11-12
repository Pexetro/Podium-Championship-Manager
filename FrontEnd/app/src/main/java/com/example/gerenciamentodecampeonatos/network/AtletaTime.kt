package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AtletaTime(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("atleta") val atleta: Atleta? = null,
    @SerializedName("time") val time: Time? = null,
    @SerializedName("data_entrada") val data_entrada: String? = null
): Serializable

data class Atleta(
    @SerializedName("idAtleta") val id_atleta: Long? = null,
    @SerializedName("pessoa") val pessoa: Pessoa? = null
): Serializable