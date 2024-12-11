package com.gonzalosanti.app_vacaciones.login.bv

data class BDItemResponse(
    val id: String,
    val id_empleado: String,
    val nombre: String,
    val fecha_inicio: String,
    val fecha_fin: String,
    val status_bloqueo: String,
    val id_solicitante: String?,
)