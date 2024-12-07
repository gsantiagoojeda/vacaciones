package com.gonzalosanti.app_vacaciones.login.av

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.databinding.ActivityAvactivityBinding
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVAdapter
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVViewHolder
import com.gonzalosanti.app_vacaciones.login.avhistory.AvHistoryActivity
import com.gonzalosanti.app_vacaciones.login.hv.HVAPIService
import com.gonzalosanti.app_vacaciones.login.hv.HVItemResponse
import com.gonzalosanti.app_vacaciones.login.hv.HVProvider
import com.gonzalosanti.app_vacaciones.login.hv.HVResponse
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV
import com.gonzalosanti.app_vacaciones.login.hv.paramHV
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AVActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var BtnHistory: ImageView
    private lateinit var binding: ActivityAvactivityBinding

    private lateinit var adapter: AVAdapter


    var svBandResp: Boolean = false
    var svErr: Boolean = false
    lateinit var arraySolicitudes: MutableList<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_avactivity)

        binding = ActivityAvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initListeners()
        loadAV()
    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
        BtnHistory = findViewById(R.id.BtnHistory)
    }


    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
        BtnHistory.setOnClickListener { navigateToHistory() }
    }

    private fun loadAV() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var depto: String = sharedPreferences.getString("departamento", "0").toString()
        var puesto: String = sharedPreferences.getString("puesto", "0").toString()
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        var empresa: String = sharedPreferences.getString("empresa", "0").toString()

        getAV(paramHV(depto, puesto, idUser, empresa), this)


    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHistory() {
        val intent = Intent(this, AvHistoryActivity::class.java)
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

    private fun getAV(params: paramHV, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(HVAPIService::class.java)
                .getSolicitudes("api/getSVDepto.php/", params)
            val resp: HVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                val solicitudes = resp?.solicitudes ?: emptyList()
                //var tam= solicitudes.size
                if (!err) {
                    arraySolicitudes = solicitudes.toMutableList()
                    if (arraySolicitudes.isNotEmpty()) {
                        runOnUiThread{
                        initRecyclerView(context)
                        }
                    }else{
                        runOnUiThread {
                            Toast.makeText(context, "No hay solicitudes", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    showError(context)
                }
            } else {
                showError(context)
            }
        }
    }

    private fun initRecyclerView(context: Context) {
        HVProvider.solicitudesList.clear()
        for (el in arraySolicitudes) {
            val solicitud: HVItemResponse =
                Gson().fromJson(el.toString(), HVItemResponse::class.java)
            var fechaInicio = solicitud.fecha_inicio
            var fechaFin = solicitud.fecha_fin
            var fechaAut: String = solicitud.fecha_autorizacion.toString()
            var status = solicitud.status_autorizacion
            var idSol = solicitud.id
            var idEmpleado: String = solicitud.id_empleado
            var idAut: String = solicitud.id_autorizador.toString()
            var totalDias = solicitud.total_dias
            var tipo = solicitud.tipo_solicitud
            HVProvider.solicitudesList.add(
                SolicitudV(
                    context,
                    idSol,
                    idEmpleado,
                    fechaInicio,
                    fechaFin,
                    status,
                    fechaAut,
                    idAut,
                    totalDias,
                    tipo,
                    ""
                )
            )
        }

        adapter = AVAdapter(HVProvider.solicitudesList)
        binding.recyclerAV.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerAV.adapter = adapter
        var tam = HVProvider.solicitudesList.size
        //  adapter.updateHV(HVProvider.solicitudesList)
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