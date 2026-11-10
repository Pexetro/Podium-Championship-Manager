package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName

data class Pessoa(
    @SerializedName("idPessoa") val id_pessoa: Long? = null,
    @SerializedName("nome") val nome: String,
    @SerializedName("cpf") val cpf: Long,
    @SerializedName("email") val email: String,
    @SerializedName("data_nascimento") val data_nascimento: String,
    @SerializedName("senha") val senha: String? = null
)