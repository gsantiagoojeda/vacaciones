package com.gonzalosanti.app_vacaciones.login.avhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVViewHolder
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV

class AVHistoryAdapter(private var solicitudesList: List<SolicitudV>) : RecyclerView.Adapter<AVHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AVHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AVHistoryViewHolder(layoutInflater.inflate(R.layout.item_avhistory, parent, false))
    }

    override fun getItemCount(): Int = solicitudesList.size

    override fun onBindViewHolder(holder: AVHistoryViewHolder, position: Int) {
        val item=solicitudesList[position]
        holder.render(item)
    }
}