package com.gonzalosanti.app_vacaciones.login.bv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.bv.BDProvider.Companion.BloqueoList
import com.gonzalosanti.app_vacaciones.login.bv.BloqueoD

class BDAdapter (private var bloqueoList: List<BloqueoD>): RecyclerView.Adapter<BDViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BDViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        return BDViewHolder(layoutInflater.inflate(R.layout.item_bd, parent, false))
    }

    override fun getItemCount(): Int = BloqueoList.size

    override fun onBindViewHolder(holder: BDViewHolder, position: Int) {
        val item=bloqueoList[position]
        holder.render(item)
    }
}