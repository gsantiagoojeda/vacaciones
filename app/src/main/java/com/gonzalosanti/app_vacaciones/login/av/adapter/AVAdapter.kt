package com.gonzalosanti.app_vacaciones.login.av.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV

class AVAdapter(private var solicitudesList: List<SolicitudV>) : RecyclerView.Adapter<AVViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AVViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AVViewHolder(layoutInflater.inflate(R.layout.item_av, parent, false))
    }

    override fun getItemCount(): Int = solicitudesList.size

    override fun onBindViewHolder(holder: AVViewHolder, position: Int) {
        val item=solicitudesList[position]
        holder.render(item)
    }
}