package com.gonzalosanti.app_vacaciones.login.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.bv.BloqueoD
import com.gonzalosanti.app_vacaciones.login.bv.BvActivity
import com.gonzalosanti.app_vacaciones.login.cv.CVCActivity

class MenuMCActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var BtnVD: Button
    private lateinit var BtnBD: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_mcactivity)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        BtnVD = findViewById(R.id.BtnVD)
        BtnBD = findViewById(R.id.BtnBD)
        BtnBack = findViewById(R.id.BtnBack)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
        BtnVD.setOnClickListener { navigateToVD() }
        BtnBD.setOnClickListener { navigateToBD() }
    }
    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToVD() {
        val intent = Intent(this, CVCActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToBD() {
        val intent = Intent(this, BvActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}