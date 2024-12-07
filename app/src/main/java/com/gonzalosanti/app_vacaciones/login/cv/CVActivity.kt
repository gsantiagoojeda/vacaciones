package com.gonzalosanti.app_vacaciones.login.cv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CVActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var dtc: TextView
    private lateinit var drc: TextView
    private lateinit var duc: TextView
    private lateinit var dte: TextView
    private lateinit var dv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv)

        initComponents()
        initUI()
        initListeners()


    }

    private fun initComponents() {
        dtc = findViewById(R.id.dtc)
        duc = findViewById(R.id.duc)
        drc = findViewById(R.id.drc)
        dte = findViewById(R.id.dte)
        dv = findViewById(R.id.dv)
        BtnBack=findViewById(R.id.BtnBack)
    }

    private fun initUI() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var user: String = sharedPreferences.getString("idUser", "0").toString()


        getDays(paramCV(user), this)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
    }


    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
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

    private fun getDays(params: paramCV, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(CVAPIService::class.java)
                .getLoginByBreeds("api/getDays.php/", params)
            val resp: CVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                if (!err) {
                    runOnUiThread {
                        var restantes: Float =
                            resp!!.diasTotales.toFloat() - resp!!.diasGozados.toFloat()
                        dtc.text = "${resp!!.diasTotales}"
                        duc.text = "${resp!!.diasGozados}"
                        dte.text = "${resp!!.diasEspeciales}"
                        dv.text = "${resp!!.diasVencidos}"
                        drc.text = "${restantes}"
                    }

                }

            } else {
                showError(context)
            }
        }
    }

    private fun showError(context: Context) {
        runOnUiThread {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}