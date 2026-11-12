package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName

data class AtletaTime(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("atleta") val atleta: Atleta? = null,
    @SerializedName("time") val time: Time? = null,
    @SerializedName("data_entrada") val data_entrada: String? = null
)

data class Atleta(
    @SerializedName("id_atleta") val id_atleta: Long? = null,
    @SerializedName("pessoa") val pessoa: Pessoa? = null
)