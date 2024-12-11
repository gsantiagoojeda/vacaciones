package com.gonzalosanti.app_vacaciones.login.bv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface HBDApiService {
    @POST
    suspend fun getBD(@Url url: String, @Body params: paramHBD): Response<HDBResponse>
}

data class  paramHBD(val depto:String,val puesto:String, val idUser:String, val empresa:String)