package com.gonzalosanti.app_vacaciones.login.hv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.avhistory.adapter.AVHistoryViewHolder
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.DATE_AUT
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.DATE_END
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.DATE_START
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.ID_AUT
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.ID_EMPLEADO
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.STATUS
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.TOTAL_DIAS
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVViewHolder.Companion.TYPE
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HVDetails : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var TVDS: TextView
    private lateinit var TVDE: TextView
    private lateinit var TVDT: TextView
    private lateinit var TVSA: TextView
    private lateinit var TVA: TextView
    private lateinit var titleTVA: TextView
    private lateinit var titleTVDA: TextView
    private lateinit var TVDA: TextView
    private lateinit var TVTS: TextView
    private lateinit var TVO: TextView

    var svBandResp:Boolean=false
    lateinit var nombre: String
    lateinit var apellidoMaterno: String
    lateinit var apellidoPaterno: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_hvdetails)
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
        TVA = findViewById(R.id.TVA)
        titleTVA = findViewById(R.id.titleTVA)
        TVDA = findViewById(R.id.TVDA)
        titleTVDA = findViewById(R.id.titleTVDA)
        TVTS = findViewById(R.id.TVTS)
        TVO = findViewById(R.id.Observaciones)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, HVActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initUI() {
        val id_empleado = intent.extras?.getString(ID_EMPLEADO) ?: "0"
        val id_aut = intent.extras?.getString(ID_AUT) ?: "0"
        val dateStart = intent.extras?.getString(DATE_START) ?: "0"
        val dateEnd = intent.extras?.getString(DATE_END) ?: "0"
        val dateAut = intent.extras?.getString(DATE_AUT) ?: "0"
        val totalDias = intent.extras?.getString(TOTAL_DIAS) ?: "0"
        val type = intent.extras?.getString(TYPE) ?: "0"
        val status = intent.extras?.getString(STATUS) ?: "0"
        val obs = intent.extras?.getString(AVHistoryViewHolder.OBS) ?: "-"

        TVDS.text = dateStart
        TVDE.text = dateEnd
        //TVDT.text = totalDias.dropLast(1)
        TVDT.text = totalDias
        TVSA.text = status
        TVTS.text = type
        TVO.text = obs

        if (status != "PENDIENTE") {
            if(status=="RECHAZADO"){
                titleTVA.text="Rechazado por"
                titleTVDA.text="Fecha de rechazo";
            }
            TVDA.text = dateAut


            getAuth(paramIdAuth(id_aut), this)


        } else {
            TVA.text = "-"
            TVDA.text = "-"
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

    private fun getAuth(params: paramIdAuth, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(AUTHAPIService::class.java)
                .getAuth("api/getAuth.php/", params)
            val resp: AUTHResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true

                if (!err) {
                    nombre = resp?.nombre ?: ""
                    apellidoPaterno = resp?.apellidoPaterno ?: ""
                    apellidoMaterno = resp?.apellidoMaterno ?: ""
                    runOnUiThread {
                        TVA.text = "$nombre $apellidoPaterno $apellidoMaterno"
                    }

                } else {
                    svBandResp = false
                }

            } else {
                runOnUiThread {
                    showError(context)
                }
            }
        }
    }

    private fun showError(context: Context) {
        runOnUiThread {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}

