package com.gonzalosanti.app_vacaciones.login.cv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface CVCAPIService {
    @POST
    suspend fun getDaysDepto(@Url url: String, @Body params: paramCVD): Response<CVCResponse>
}
data class  paramCVD(val depto:String, val puesto:String, val idUser:String, val empresa:String)