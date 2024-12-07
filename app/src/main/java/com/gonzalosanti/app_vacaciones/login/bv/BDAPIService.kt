package com.gonzalosanti.app_vacaciones.login.bv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface BDAPIService {
    @POST
    suspend fun setBD(@Url url: String, @Body params: paramBD): Response<BDResponse>
}

data class  paramBD(val idUser:String,val dateStart: String, val dateEnd: String, val empleados:  List<String>)