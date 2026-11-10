package com.example.gerenciamentodecampeonatos.network

import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Query

interface ApiService {

    @POST("pessoas/cadastro")
    suspend fun cadastrarPessoa(@Body pessoa: Pessoa): Response<Pessoa>

    @POST("pessoas/login")
    suspend fun loginPessoa(@Body pessoa: Pessoa): Response<Pessoa>


    @GET("pessoas")
    suspend fun listarPessoas(): Response<List<Pessoa>>

    @GET("pessoas/{id}")
    suspend fun buscarPessoa(@Path("id") id: Long): Response<Pessoa>

    @PUT("pessoas/{id}")
    suspend fun atualizarPessoa(@Path("id") id: Long, @Body pessoa: Pessoa): Response<Pessoa>
}
