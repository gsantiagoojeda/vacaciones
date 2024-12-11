package com.gonzalosanti.app_vacaciones.login.sv

import com.gonzalosanti.app_vacaciones.login.cv.CVResponse
import com.gonzalosanti.app_vacaciones.login.cv.paramCV
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface SVAPIService {
    @POST
    suspend fun createSVacation(@Url url: String, @Body params: paramSV): Response<SVResponse>
}

data class paramSV(val id: String, val dateStart: String, val dateEnd: String)