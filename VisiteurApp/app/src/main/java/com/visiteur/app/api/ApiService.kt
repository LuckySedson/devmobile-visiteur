package com.visiteur.app.api

import com.visiteur.app.model.Visiteur
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("visiteurs")
    suspend fun getVisiteurs(): Response<List<Visiteur>>

    @Headers("Content-Type: application/json")
    @POST("visiteurs")
    suspend fun createVisiteur(@Body visiteur: Visiteur): Response<Visiteur>

    @Headers("Content-Type: application/json")
    @PUT("visiteurs/{id}")
    suspend fun updateVisiteur(
        @Path("id") id: Long,
        @Body visiteur: Visiteur
    ): Response<Visiteur>

    @DELETE("visiteurs/{id}")
    suspend fun deleteVisiteur(@Path("id") id: Long): Response<Void>

    @GET("visiteurs/stats")
    suspend fun getStats(): Response<Map<String, Double>>
}