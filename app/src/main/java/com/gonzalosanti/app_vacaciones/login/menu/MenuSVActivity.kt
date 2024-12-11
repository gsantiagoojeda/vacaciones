package com.gonzalosanti.app_vacaciones.login.menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.LoginActivity
import com.gonzalosanti.app_vacaciones.login.sv.SEActivity
import com.gonzalosanti.app_vacaciones.login.sv.SEOActivity
import com.gonzalosanti.app_vacaciones.login.sv.SVActivity
import com.gonzalosanti.app_vacaciones.login.sv.SVOActivity

class MenuSVActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var BtnVD: Button
    private lateinit var BtnED: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_svactivity)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        BtnVD = findViewById(R.id.BtnVD)
        BtnED = findViewById(R.id.BtnED)
        BtnBack = findViewById(R.id.BtnBack)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
        BtnVD.setOnClickListener { navigateToVD() }
        BtnED.setOnClickListener { navigateToED() }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToVD() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        val perfil: String = sharedPreferences.getString("perfilUser", "0").toString()
        Log.i("gonzalito", perfil)
       // val empresa: String = sharedPreferences.getString("empresa", "0").toString()

        if (perfil == "1") {
            val intent = Intent(this, SVOActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, SVActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToED() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        val perfil: String = sharedPreferences.getString("perfilUser", "0").toString()
        if (perfil == "1") {
            val intent = Intent(this, SEOActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, SEActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}