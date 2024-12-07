package com.gonzalosanti.app_vacaciones.login.bv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.bv.adapter.BDViewHolder.Companion.ID_BLOQUEO
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RBDActivity : AppCompatActivity() {
    private lateinit var TvMessage: TextView
    private lateinit var  BtnAccept: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_rbdactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idBloqueo: String = intent.extras?.getString(ID_BLOQUEO)?: "0"

        initComponents()
        initUI(idBloqueo)
        initListeners()
    }

    private fun initComponents(){
        TvMessage = findViewById(R.id.TVMessage)
        BtnAccept = findViewById(R.id.BtnAccept)
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

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gpoalze.cloud/vacaciones/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor()).build()

    private fun initUI(idBloqueo: String){
        setDB(paramDB(idBloqueo), this)
    }

    private fun setDB(params: paramDB, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(DBAPIService::class.java)
                .setDB("api/setDB.php/", params)
            val resp: DBResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                val statusText = resp?.statusText ?: "Error desconocido"

                if (!err) {
                    TvMessage.text=statusText
                    TvMessage.setTextColor(ContextCompat.getColor(context, R.color.color_success))
                }else{
                    TvMessage.text=statusText
                    TvMessage.setTextColor(ContextCompat.getColor(context, R.color.color_danger))
                }

            }
        }
    }
}