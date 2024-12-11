package com.gonzalosanti.app_vacaciones.login.hv

import android.content.Context

data class SolicitudV(
    val context: Context,
    val id: String,
    val idEmpleado: String,
    val dateStart: String,
    val dateEnd: String,
    val status: String,
    val dateAut: String,
    val idAut: String,
    val totalDias: String,
    val type: String,
    val observaciones: String
)