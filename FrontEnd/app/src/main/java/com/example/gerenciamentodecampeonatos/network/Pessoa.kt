package com.example.gerenciamentodecampeonatos.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pessoa(
    @SerializedName("id_pessoa") val id_pessoa: Long? = null,
    @SerializedName("nome") val nome: String,
    @SerializedName("cpf") val cpf: Long,
    @SerializedName("email") val email: String,
    @SerializedName("dataNascimento") val data_nascimento: String,
    @SerializedName("senha") val senha: String? = null
) : Serializable