package com.example.gerenciamentodecampeonatos.network

data class Pessoa(
    val id_pessoa: Long? = null,
    val nome: String,
    val cpf: Long,
    val email: String,
    val data_nascimento: String, // Ex: "2000-01-01"
    val senha: String? = null
)