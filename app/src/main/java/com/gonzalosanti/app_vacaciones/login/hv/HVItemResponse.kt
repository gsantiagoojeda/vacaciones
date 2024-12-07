package com.gonzalosanti.app_vacaciones.login.hv

data class HVItemResponse (
    val id: String,
    val id_empleado: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val status_autorizacion: String,
    val fecha_autorizacion: String?,
    val id_autorizador: String?,
    val total_dias: String,
    val tipo_solicitud: String,
    val observaciones: String,
)