package com.gonzalosanti.app_vacaciones.login.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.LoginActivity
import com.gonzalosanti.app_vacaciones.login.av.AVActivity
import com.gonzalosanti.app_vacaciones.login.bv.BvActivity
import com.gonzalosanti.app_vacaciones.login.bv.HBDActivity
import com.gonzalosanti.app_vacaciones.login.cv.CVActivity
import com.gonzalosanti.app_vacaciones.login.hv.HVActivity
import com.gonzalosanti.app_vacaciones.login.sv.SVActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var TvWelcome: TextView
    private lateinit var BtnBack: ImageView
    private lateinit var CardSV: CardView
    private lateinit var CardCV: CardView
    private lateinit var CardAV: CardView
    private lateinit var CardVV: CardView
    private lateinit var CardBV: CardView
    private lateinit var CardHBD: CardView
    private lateinit var CardSVClick: LinearLayout
    private lateinit var CardCVClick: LinearLayout
    private lateinit var CardAVClick: LinearLayout
    private lateinit var CardVVClick: LinearLayout
    private lateinit var CardBVClick: LinearLayout
    private lateinit var CardHBDClick: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()
        initListeners()
    }

    private fun initUI(){
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var nombre: String = sharedPreferences.getString("nombre", "0").toString()
        var apellidoP: String = sharedPreferences.getString("apellidoP", "0").toString()
        var tipoUser: String = sharedPreferences.getString("tipoUser", "0").toString()

        TvWelcome.text="Bienvenido: ${nombre} ${apellidoP}"

        when(tipoUser){
            "1","4"->{
                CardAV.alpha = 0.5f
                CardAV.isClickable=false
                CardAV.isEnabled=false
                CardAV.isFocusable = false
                CardAVClick.isClickable=false
                CardAVClick.isEnabled=false
                CardAVClick.isFocusable = false

                CardBV.alpha = 0.5f
                CardBV.isClickable=false
                CardBV.isEnabled=false
                CardBV.isFocusable = false
                CardBVClick.isClickable=false
                CardBVClick.isEnabled=false
                CardBVClick.isFocusable = false

                CardHBD.alpha = 0.5f
                CardHBD.isClickable=false
                CardHBD.isEnabled=false
                CardHBD.isFocusable = false
                CardHBDClick.isClickable=false
                CardHBDClick.isEnabled=false
                CardHBDClick.isFocusable = false
            }
        }


    }

    private fun initComponents() {
        TvWelcome = findViewById(R.id.TvWelcome)
        BtnBack = findViewById(R.id.BtnBack)
        CardSV = findViewById(R.id.CardSV)
        CardAV = findViewById(R.id.CardAV)
        CardCV = findViewById(R.id.CardCV)
        CardVV = findViewById(R.id.CardVV)
        CardBV = findViewById(R.id.CardBV)
        CardHBD = findViewById(R.id.CardHBD)
        CardSVClick = findViewById(R.id.CardSVClick)
        CardAVClick = findViewById(R.id.CardAVClick)
        CardCVClick = findViewById(R.id.CardCVClick)
        CardVVClick = findViewById(R.id.CardVVClick)
        CardBVClick = findViewById(R.id.CardBVClick)
        CardHBDClick = findViewById(R.id.CardHBDClick)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToLogin() }
        CardSVClick.setOnClickListener { navigateToSV() }
        CardAVClick.setOnClickListener { navigateToAV() }
        CardVVClick.setOnClickListener { navigateToVV() }
        CardCVClick.setOnClickListener { navigateToCV() }
        CardBVClick.setOnClickListener { navigateToBV() }
        CardHBDClick.setOnClickListener { navigateToHBD() }
    }

    private fun navigateToLogin(){
        val intent= Intent(this, LoginActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSV(){
        val intent= Intent(this,  MenuSVActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAV(){
        val intent= Intent(this, AVActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToCV(){
        val intent= Intent(this, CVActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToVV(){
        val intent= Intent(this, HVActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToBV(){
        val intent= Intent(this, BvActivity:: class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHBD(){
        val intent= Intent(this, HBDActivity:: class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        navigateToLogin()
        if(false) {
            super.onBackPressed()
        }
    }



}