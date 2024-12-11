package com.gonzalosanti.app_vacaciones.login.av.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.databinding.ItemAvBinding
import com.gonzalosanti.app_vacaciones.databinding.ItemHvBinding
import com.gonzalosanti.app_vacaciones.login.av.AVDetails
import com.gonzalosanti.app_vacaciones.login.hv.HVDetails
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder

class AVViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemAvBinding.bind(view)

    companion object{
        const val ID="ID"
        const val ID_EMPLEADO="ID_EMPLEADO"
        const val DATE_START="DATE_START"
        const val DATE_END="DATE_END"
        const val STATUS="STATUS"
        const val DATE_AUT="DATE_AUT"
        const val ID_AUT="ID_AUT"
        const val TOTAL_DIAS="TOTAL_DIAS"
        const val TYPE="TYPE"
    }

    fun render(solicitudV: SolicitudV) {
        binding.itemDS.text = solicitudV.dateStart
        binding.itemDE.text = solicitudV.dateEnd
        binding.itemSA.text = solicitudV.status
        binding.itemE.text=solicitudV.idEmpleado

        binding.ItemHVButton.setOnClickListener {
            val intent = Intent(solicitudV.context, AVDetails::class.java)
            intent.putExtra(ID,solicitudV.id)
            intent.putExtra(ID_EMPLEADO,solicitudV.idEmpleado)
            intent.putExtra(DATE_START,solicitudV.dateStart)
            intent.putExtra(DATE_END,solicitudV.dateEnd)
            intent.putExtra(DATE_AUT,solicitudV.dateAut)
            intent.putExtra(STATUS,solicitudV.status)
            intent.putExtra(ID_AUT,solicitudV.idAut)
            intent.putExtra(TOTAL_DIAS,solicitudV.totalDias)
            intent.putExtra(TYPE,solicitudV.type)
            solicitudV.context.startActivity(intent)
        }
    }
}