package com.gonzalosanti.app_vacaciones.login.bv

import com.gonzalosanti.app_vacaciones.login.hv.HVResponse
import com.gonzalosanti.app_vacaciones.login.hv.paramHV
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface EDAPIService {
    @POST
    suspend fun getEmpleados(@Url url: String, @Body params: paramED): Response<EDResponse>
}

data class  paramED(val depto:String,val puesto:String, val idUser:String, val empresa:String)