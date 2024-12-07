package com.gonzalosanti.app_vacaciones.login.login

import android.util.Log
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface APIService {

    @POST
    suspend fun getLoginByBreeds(@Url url: String, @Body params:paramLogin): Response<UserResponse>

}

data class  paramLogin(val id:String, val passw:String)