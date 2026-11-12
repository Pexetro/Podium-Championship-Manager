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
    suspend fun listarTodasPessoas(): Response<List<Pessoa>>

    @GET("pessoas/{id}")
    suspend fun buscarPessoa(@Path("id") id: Long): Response<Pessoa>

    @GET("times/{id}")
    suspend fun buscarTimePorId(@Path("id") id: Long): Response<Time>

    @GET("campeonatos")
    suspend fun listarCampeonatos(): Response<List<Campeonato>>

    @GET("jogos/campeonato/{id}")
    suspend fun listarJogosDoCampeonato(@Path("id") idCampeonato: Long): Response<List<Jogo>>

    @GET("times")
    suspend fun listarTimes(): Response<List<Time>>

    @GET("atletas-times/time/{timeId}")
    suspend fun listarAtletasDoTime(@Path("timeId") timeId: Long): Response<List<AtletaTime>>

    @GET("campeonatos/{id}")
    suspend fun buscarCampeonatoPorId(@Path("id") id: Long): Response<Campeonato>
    @GET("campeonato-times/campeonato/{id}")
    suspend fun listarTimesDoCampeonato(@Path("id") id: Long): Response<List<CampeonatoTime>>

    @POST("campeonatos")
    suspend fun criarCampeonato(@Body campeonato: Campeonato, @Query("criadorId") criadorId: Long): Response<Campeonato>


    @POST("times") suspend fun criarTime(@Body time: Time, @Query("criadorId") id: Long): Response<Time>


    @POST("atletas")
    suspend fun criarAtleta(@Body atleta: Atleta): Response<Atleta>

    @POST("atletas-times")
    suspend fun adicionarAtletaAoTime(
        @Query("timeId") timeId: Long,
        @Query("atletaId") atletaId: Long,
        @Query("usuarioSolicitanteId") usuarioSolicitanteId: Long
    ): Response<AtletaTime>

    @POST("jogos")
    suspend fun criarJogo(@Body jogo: Jogo): Response<Jogo>

    @POST("campeonato-times")
    suspend fun inscreverTimeNoCampeonato(@Body campeonatoTime: CampeonatoTime): Response<CampeonatoTime>

    @PUT("pessoas/{id}")
    suspend fun atualizarPessoa(@Path("id") id: Long, @Body pessoa: Pessoa): Response<Pessoa>

    @PUT("jogos/{id}/placar")
    suspend fun atualizarPlacar(
        @Path("id") idJogo: Long,
        @Body dadosPlacar: Jogo,
        @Query("usuarioId") usuarioId: Long // O seu Java espera o nome "usuarioId"
    ): Response<Jogo>

    @DELETE("atletas-times/{id}")
    suspend fun removerAtleta(@Path("id") idVinculo: Long, @Query("usuarioSolicitanteId") usuarioId: Long): Response<Void>



}
