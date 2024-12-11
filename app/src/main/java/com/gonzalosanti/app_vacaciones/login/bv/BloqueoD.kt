package com.gonzalosanti.app_vacaciones.login.bv

import android.content.Context

data class BloqueoD (
    val context: Context,
    val id: String,
    val idEmpleado: String,
    val nombre: String,
    val dateStart: String,
    val dateEnd: String,
    val status: String
)