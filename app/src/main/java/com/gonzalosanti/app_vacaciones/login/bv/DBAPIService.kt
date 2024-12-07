package com.gonzalosanti.app_vacaciones.login.bv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface DBAPIService {
    @POST
    suspend fun setDB(@Url url: String, @Body params: paramDB): Response<DBResponse>
}

data class  paramDB(val id:String)