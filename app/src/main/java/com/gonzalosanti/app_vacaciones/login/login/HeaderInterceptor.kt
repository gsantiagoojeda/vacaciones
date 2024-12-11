package com.gonzalosanti.app_vacaciones.login.login

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request().newBuilder().addHeader("Accept", "application/json")
        //se pueden agregar .addHeader como sean necesarios
            .build()

        return chain.proceed(request)
    }

}