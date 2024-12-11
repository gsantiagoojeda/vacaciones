package com.gonzalosanti.app_vacaciones.login.av

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVViewHolder
import com.gonzalosanti.app_vacaciones.login.hv.AUTHResponse
import com.gonzalosanti.app_vacaciones.login.hv.HVActivity
import com.gonzalosanti.app_vacaciones.login.hv.paramIdAuth
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AVDetails : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var BtnAuthorize: Button
    private lateinit var BtnDecline: Button
    private lateinit var TVDS: TextView
    private lateinit var TVDE: TextView
    private lateinit var TVDT: TextView
    private lateinit var TVSA: TextView
    private lateinit var TVA: TextView
    private lateinit var titleTVA: TextView
    private lateinit var titleTVDA: TextView
    private lateinit var TVDA: TextView
    private lateinit var TVTS: TextView
    private lateinit var TVE: TextView
    private lateinit var TEO: AppCompatEditText

    private lateinit var idSV: String
    private lateinit var idAuth: String

    var svBandResp: Boolean = false
    lateinit var statusText: String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_avdetails)

        initComponents()
        initUI()
        initListeners()

    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
        BtnAuthorize = findViewById(R.id.BtnAuthorize)
        BtnDecline = findViewById(R.id.BtnDecline)
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
        TEO= findViewById(R.id.Observaciones)
    }

    private fun initUI() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        idAuth = sharedPreferences.getString("idUser", "0").toString()


        val id_empleado = intent.extras?.getString(AVViewHolder.ID_EMPLEADO) ?: "0"
        idSV = intent.extras?.getString(AVViewHolder.ID) ?: "0"
        val dateStart = intent.extras?.getString(AVViewHolder.DATE_START) ?: "0"
        val dateEnd = intent.extras?.getString(AVViewHolder.DATE_END) ?: "0"
        val dateAut = intent.extras?.getString(AVViewHolder.DATE_AUT) ?: "0"
        val totalDias = intent.extras?.getString(AVViewHolder.TOTAL_DIAS) ?: "0"
        val type = intent.extras?.getString(AVViewHolder.TYPE) ?: "0"
        val status = intent.extras?.getString(AVViewHolder.STATUS) ?: "0"

        TVDS.text = dateStart
        TVDE.text = dateEnd
        //TVDT.text = totalDias.dropLast(1)
        TVDT.text = totalDias
        TVSA.text = status
        TVTS.text = type
        TVE.text = id_empleado

        if (status != "Pendiente") {
            Log.i("gonzalito", "Btns false")
            BtnAuthorize.isEnabled = false
            BtnDecline.isEnabled = false
            BtnAuthorize.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.color_fondo_gris)
            BtnDecline.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.color_fondo_gris)
            BtnAuthorize.setTextColor(Color.WHITE)
            BtnDecline.setTextColor(Color.WHITE)
            if (status == "Rechazada") {
                titleTVDA.text = "Fecha de rechazo";
            }
            TVDA.text = dateAut


        } else {
            Log.i("gonzalito", "Btns true")
            TVDA.text = "-"
        }

    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
        BtnAuthorize.setOnClickListener {
            BtnAuthorize.isEnabled = false
            BtnDecline.isEnabled = false
            val obs= TEO.text.toString()

            authSv(paramAV(idSV, idAuth, "Autorizada", obs), this)
        }

        BtnDecline.setOnClickListener {
            BtnAuthorize.isEnabled = false
            BtnDecline.isEnabled = false
            val obs= TEO.text.toString()

            authSv(paramAV(idSV, idAuth, "Rechazada", obs), this)

        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, AVActivity::class.java)
        startActivity(intent)
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

    private fun authSv(params: paramAV, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(AVAPIService::class.java)
                .getAV("api/authSV.php/", params)
            val resp: AVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true

                if (!err) {
                    runOnUiThread {
                        statusText = resp?.statusText ?: ""
                        val status= params.status
                        Toast.makeText(context, "Solicitud $status", Toast.LENGTH_SHORT).show()
                        val handler = Handler(Looper.getMainLooper())
                        val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)
                        handler.postDelayed({
                            navigateToMenu()
                        }, delayMillis)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(context, "ERROR AL PROCESAR", Toast.LENGTH_SHORT).show()
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