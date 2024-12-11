package com.gonzalosanti.app_vacaciones.login.sv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.gonzalosanti.app_vacaciones.login.sv.SVActivity.Companion.DAYS_TOTAL
import com.gonzalosanti.app_vacaciones.login.sv.SVActivity.Companion.DAYS_USED

class RSVActivity : AppCompatActivity() {
    private lateinit var TvDays: TextView
    private lateinit var  BtnAccept: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rsvactivity)

        val totalDays: String = intent.extras?.getString(DAYS_TOTAL)?: "0"
        val usedDays: String = intent.extras?.getString(DAYS_USED)?: "0"

        initComponents()
        initUI(usedDays,totalDays)
        initListeners()
    }

    private fun initComponents(){
        TvDays = findViewById(R.id.TvDays)
        BtnAccept = findViewById(R.id.BtnAccept)
    }

    private fun initUI(usedDays:String, totaldays:String){
        TvDays.text="$usedDays / $totaldays"
    }

    private fun initListeners(){
        BtnAccept.setOnClickListener {goToMenu() }
    }


    private  fun goToMenu(){
        val intent= Intent(this, MenuActivity:: class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        goToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}