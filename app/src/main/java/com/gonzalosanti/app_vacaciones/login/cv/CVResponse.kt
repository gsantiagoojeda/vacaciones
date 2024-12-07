package com.gonzalosanti.app_vacaciones.login.cv

data class CVResponse (
    var err : Boolean,
    var idEmpleado:String,
    var diasTotales:String,
    var diasEspeciales:String,
    var diasGozados:String,
    var pendVacacion:String,
    var pendEspecial:String,
    var diasVencidos:String,
    var antiguedad:String,

)