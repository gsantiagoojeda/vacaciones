package com.gonzalosanti.app_vacaciones.login.cv

data class CVCItemResponse (
    val id: String,
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val departamento: String,
    val diasTotales: String,
    val diasGozados: String,
    val diasEspeciales: String,
    val diasVencidos: String,
    val pendVacacion: String,
    val pendEspecial: String,
    val antiguedad: String,
)