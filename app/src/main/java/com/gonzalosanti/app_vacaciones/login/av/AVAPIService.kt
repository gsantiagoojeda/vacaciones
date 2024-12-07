package com.gonzalosanti.app_vacaciones.login.av


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface AVAPIService {
    @POST
    suspend fun getAV(@Url url: String, @Body params: paramAV): Response<AVResponse>
}

data class  paramAV(val id:String, val idAuth:String, val status: String, val obs:String)