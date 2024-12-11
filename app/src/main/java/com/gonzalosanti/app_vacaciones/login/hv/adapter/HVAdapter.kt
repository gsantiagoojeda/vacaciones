package com.gonzalosanti.app_vacaciones.login.hv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV

class HVAdapter(private var solicitudesList: List<SolicitudV>) : RecyclerView.Adapter<HVViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HVViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HVViewHolder(layoutInflater.inflate(R.layout.item_hv, parent, false))
    }

    override fun getItemCount(): Int = solicitudesList.size

    override fun onBindViewHolder(holder: HVViewHolder, position: Int) {
        val item=solicitudesList[position]
        holder.render(item)
    }

    fun updateHV(solicitudesList: List<SolicitudV>){
        this.solicitudesList=solicitudesList
        notifyDataSetChanged()
    }
}