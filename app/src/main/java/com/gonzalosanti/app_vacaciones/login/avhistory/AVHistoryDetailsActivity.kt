package com.gonzalosanti.app_vacaciones.login.avhistory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.av.AVActivity
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVViewHolder
import com.gonzalosanti.app_vacaciones.login.avhistory.adapter.AVHistoryViewHolder
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity

class AVHistoryDetailsActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var TVDS: TextView
    private lateinit var TVDE: TextView
    private lateinit var TVDT: TextView
    private lateinit var TVSA: TextView
    private lateinit var titleTVDA: TextView
    private lateinit var TVDA: TextView
    private lateinit var TVTS: TextView
    private lateinit var TVE: TextView
    private lateinit var TVO: TextView

    private lateinit var idSV: String
    private lateinit var idAuth: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avhistory_details)


        initComponents()
        initUI()
        initListeners()

    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
        TVDS = findViewById(R.id.TVDS)
        TVDE = findViewById(R.id.TVDE)
        TVDT = findViewById(R.id.TVDT)
        TVSA = findViewById(R.id.TVSA)
        //TVA = findViewById(R.id.TVA)
        //titleTVA = findViewById(R.id.titleTVA)
        TVDA = findViewById(R.id.TVDA)
        titleTVDA = findViewById(R.id.titleTVDA)
        TVTS = findViewById(R.id.TVTS)
        TVE = findViewById(R.id.TVE)
        TVO = findViewById(R.id.Observaciones)
    }

    private fun initUI() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        idAuth = sharedPreferences.getString("idUser", "0").toString()


        val id_empleado = intent.extras?.getString(AVHistoryViewHolder.ID_EMPLEADO) ?: "0"
        idSV = intent.extras?.getString(AVHistoryViewHolder.ID) ?: "0"
        val dateStart = intent.extras?.getString(AVHistoryViewHolder.DATE_START) ?: "0"
        val dateEnd = intent.extras?.getString(AVHistoryViewHolder.DATE_END) ?: "0"
        val dateAut = intent.extras?.getString(AVHistoryViewHolder.DATE_AUT) ?: "0"
        val totalDias = intent.extras?.getString(AVHistoryViewHolder.TOTAL_DIAS) ?: "0"
        val type = intent.extras?.getString(AVHistoryViewHolder.TYPE) ?: "0"
        val status = intent.extras?.getString(AVHistoryViewHolder.STATUS) ?: "0"
        val obs = intent.extras?.getString(AVHistoryViewHolder.OBS) ?: "-"

        TVDS.text = dateStart
        TVDE.text = dateEnd
        //TVDT.text = totalDias.dropLast(1)
        TVDT.text = totalDias
        TVSA.text = status
        TVTS.text = type
        TVE.text = id_empleado
        TVO.text = obs


        if (status == "Rechazada") {
            titleTVDA.text = "Fecha de rechazo";
        }
        TVDA.text = dateAut


    }


    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToHAV() }
    }

    private fun navigateToHAV() {
        val intent = Intent(this, AvHistoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        navigateToHAV()
        if(false) {
            super.onBackPressed()
        }
    }
}