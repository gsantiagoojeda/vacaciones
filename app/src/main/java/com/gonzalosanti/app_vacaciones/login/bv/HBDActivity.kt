package com.gonzalosanti.app_vacaciones.login.bv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.databinding.ActivityHbdactivityBinding
import com.gonzalosanti.app_vacaciones.login.bv.adapter.BDAdapter
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HBDActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var binding: ActivityHbdactivityBinding

    private lateinit var adapter: BDAdapter

    lateinit var arrayBloqueos: MutableList<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHbdactivityBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
        loadHB()
    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
    }

    private fun loadHB(){
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var depto: String = sharedPreferences.getString("departamento", "0").toString()
        var puesto: String = sharedPreferences.getString("puesto", "0").toString()
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        var empresa: String = sharedPreferences.getString("empresa", "0").toString()

        getHB(paramHBD(depto,puesto,idUser,empresa), this)
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

    private fun getHB(params:paramHBD,context: Context){
        CoroutineScope(Dispatchers.IO).launch{
            val call = getRetrofit().create(HBDApiService::class.java).getBD("api/getBD.php/", params)
            val resp: HDBResponse?  = call.body()

            if (call.isSuccessful){
                val err = resp?.err ?: true
                val bloqueos = resp?.bloqueos ?: emptyList()

                if (!err){
                    arrayBloqueos = bloqueos.toMutableList()
                    if (arrayBloqueos.isNotEmpty()) {
                        runOnUiThread {
                            initRecyclerView(context)
                        }
                    }else {
                        runOnUiThread {
                            Toast.makeText(context, "No hay Bloqueos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    showError(context)
                }

            }else{
                showError(context)
            }
        }
    }

    private fun initRecyclerView(context: Context) {
        BDProvider.BloqueoList.clear()

        for(el in arrayBloqueos){
            val bloqueo: BDItemResponse = Gson().fromJson(el.toString(), BDItemResponse::class.java)
            var fechaInicio = bloqueo.fecha_inicio
            var fechaFin = bloqueo.fecha_fin
            var status = bloqueo.status_bloqueo
            var idBD = bloqueo.id
            var idEmpleado = bloqueo.id_empleado
            var nombre: String = bloqueo.nombre
            nombre = nombre.replace("-", " ")

            BDProvider.BloqueoList.add(BloqueoD(this,idBD,idEmpleado,nombre,fechaInicio,fechaFin,status))
        }

        adapter= BDAdapter(BDProvider.BloqueoList)
        binding.recyclerBD.layoutManager= GridLayoutManager(this, 1)
        binding.recyclerBD.adapter=adapter
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