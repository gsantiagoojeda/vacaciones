package com.gonzalosanti.app_vacaciones.login.bv

data class EDResponse (
    var err : Boolean,
    var empleados:List<Empleado>,
)