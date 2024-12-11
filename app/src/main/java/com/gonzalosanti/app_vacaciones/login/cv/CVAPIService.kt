package com.gonzalosanti.app_vacaciones.login.cv

import com.gonzalosanti.app_vacaciones.login.login.UserResponse
import com.gonzalosanti.app_vacaciones.login.login.paramLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface CVAPIService {
    @POST
    suspend fun getLoginByBreeds(@Url url: String, @Body params: paramCV): Response<CVResponse>

}

data class  paramCV(val id:String)