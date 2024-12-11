package com.gonzalosanti.app_vacaciones.login.bv.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.databinding.ItemBdBinding
import com.gonzalosanti.app_vacaciones.databinding.ItemHvBinding
import com.gonzalosanti.app_vacaciones.login.bv.BloqueoD
import com.gonzalosanti.app_vacaciones.login.bv.RBDActivity
import com.gonzalosanti.app_vacaciones.login.hv.HVDetails
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder

class BDViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemBdBinding.bind(view)

    companion object {
        const val ID_BLOQUEO = "ID_BLOQUEO"
    }

    fun render(BloqueoD: BloqueoD) {
        binding.itemDS.text = BloqueoD.dateStart
        binding.itemDE.text = BloqueoD.dateEnd
        binding.itemEM.text = BloqueoD.nombre

        binding.ItemHVButton.setOnClickListener {
            val intent = Intent(BloqueoD.context, RBDActivity::class.java)
            intent.putExtra(ID_BLOQUEO, BloqueoD.id)

            BloqueoD.context.startActivity(intent)
        }
    }
}