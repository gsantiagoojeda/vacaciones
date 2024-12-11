package com.gonzalosanti.app_vacaciones.login.sv

 data class SVResponse (
     var err : Boolean,
     var statusText:String,
     var countDays:String,
     var totalDays:String,
     var usedDays:String
 )