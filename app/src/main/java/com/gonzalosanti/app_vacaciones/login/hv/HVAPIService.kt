package com.gonzalosanti.app_vacaciones.login.hv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface HVAPIService {
    @POST
    suspend fun getSolicitudes(@Url url: String, @Body params: paramHV): Response<HVResponse>
}

data class  paramHV(val id:String,val puesto:String, val idUser:String, val empresa:String)