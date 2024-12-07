package com.gonzalosanti.app_vacaciones.login.hv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.databinding.ActivityHvactivityBinding
import com.gonzalosanti.app_vacaciones.login.cv.CVAPIService
import com.gonzalosanti.app_vacaciones.login.cv.CVResponse
import com.gonzalosanti.app_vacaciones.login.cv.paramCV
import com.gonzalosanti.app_vacaciones.login.hv.adapter.HVAdapter
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HVActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var binding: ActivityHvactivityBinding

    private lateinit var adapter: HVAdapter

    lateinit var arraySolicitudes: MutableList<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initListeners()
        loadHV()

    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
    }


    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
    }

    private fun loadHV() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var user: String = sharedPreferences.getString("idUser", "0").toString()
        var puesto: String = sharedPreferences.getString("puesto", "0").toString()
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        var empresa: String = sharedPreferences.getString("empresa", "0").toString()

        getHV(paramHV(user, puesto, idUser,empresa), this)

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

    private fun getHV(params: paramHV, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(HVAPIService::class.java)
                .getSolicitudes("api/getSV.php/", params)
            val resp: HVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                val solicitudes = resp?.solicitudes ?: emptyList()

                if (!err) {
                    arraySolicitudes = solicitudes.toMutableList()
                    if (arraySolicitudes.isNotEmpty()) {
                        runOnUiThread {
                            initRecyclerView(context)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(context, "No hay solicitudes", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    showError()
                }

            } else {
                showError()
            }
        }
    }

    private fun initRecyclerView(context: Context) {
        HVProvider.solicitudesList.clear()
        for (el in arraySolicitudes) {
             Log.i("gonzalito","$el")
            val solicitud: HVItemResponse =
                Gson().fromJson(el.toString(), HVItemResponse::class.java)
            var fechaInicio = solicitud.fecha_inicio
            var fechaFin = solicitud.fecha_fin
            var fechaAut: String = solicitud.fecha_autorizacion.toString()
            var status = solicitud.status_autorizacion
            var idSol = solicitud.id
            var idEmpleado = solicitud.id_empleado
            var idAut: String = solicitud.id_autorizador.toString()
            var totalDias = solicitud.total_dias
            var tipo = solicitud.tipo_solicitud
            var obs :String= solicitud.observaciones
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
                    obs
                )
            )
        }

        adapter = HVAdapter(HVProvider.solicitudesList)
        binding.recyclerSV.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerSV.adapter = adapter
        var tam = HVProvider.solicitudesList.size
        //  adapter.updateHV(HVProvider.solicitudesList)
    }

    private fun showError() {
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