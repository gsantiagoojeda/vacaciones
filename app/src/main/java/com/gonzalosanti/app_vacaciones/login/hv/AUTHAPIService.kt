package com.gonzalosanti.app_vacaciones.login.hv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface AUTHAPIService {   @POST
suspend fun getAuth(@Url url: String, @Body params: paramIdAuth): Response<AUTHResponse>
}

data class  paramIdAuth(val id:String)