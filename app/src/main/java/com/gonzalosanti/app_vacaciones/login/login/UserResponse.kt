package com.gonzalosanti.app_vacaciones.login.login

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

//data class UserResponse (@SerializedName("results") var pokes:Array<Any>)
//data class UserResponse (@SerializedName("0") var user:Any)

data class UserResponse(
     var err : Boolean,
     var id : String,
     var nombre : String,
     var apellidoPaterno : String,
     var empresa : String,
     var tipoUser : String,
     var perfilUser : String,
     var fechaIngreso : String,
     var status: String,
     var departamento : String,
     var puesto : String,
     var causa : String,
     var changePass:Boolean
)