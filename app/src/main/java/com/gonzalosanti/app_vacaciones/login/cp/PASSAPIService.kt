package com.gonzalosanti.app_vacaciones.login.cp

import com.gonzalosanti.app_vacaciones.login.hv.AUTHResponse
import com.gonzalosanti.app_vacaciones.login.hv.paramIdAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface PASSAPIService {  @POST
suspend fun setPass(@Url url: String, @Body params: paramNewPass): Response<PassResponse>
}

data class  paramNewPass(val id:String, val pass:String)