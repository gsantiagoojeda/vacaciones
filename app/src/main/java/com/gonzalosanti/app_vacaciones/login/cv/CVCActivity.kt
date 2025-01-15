package com.gonzalosanti.app_vacaciones.login.cv

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.databinding.ActivityCvcactivityBinding
import com.gonzalosanti.app_vacaciones.login.av.adapter.AVAdapter
import com.gonzalosanti.app_vacaciones.login.bv.EDAPIService
import com.gonzalosanti.app_vacaciones.login.bv.EDResponse
import com.gonzalosanti.app_vacaciones.login.bv.paramED
import com.gonzalosanti.app_vacaciones.login.hv.HVItemResponse
import com.gonzalosanti.app_vacaciones.login.hv.HVProvider
import com.gonzalosanti.app_vacaciones.login.hv.SolicitudV
import com.gonzalosanti.app_vacaciones.login.hv.paramHV
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.gonzalosanti.app_vacaciones.login.menu.MenuMCActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Empleado(
    val nombre: String,
    var diasTotales: Float,
    var diasGozados: Float,
    var diasEspeciales: Float
)

class CVCActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var Table: TableLayout


    lateinit var arrayEmp: MutableList<Any>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cvcactivity)
        initComponents()
        initListeners()
        loadVC()
    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
        Table = findViewById(R.id.Table)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToMenu() }
    }

    private fun loadVC() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var depto: String = sharedPreferences.getString("departamento", "0").toString()
        var puesto: String = sharedPreferences.getString("puesto", "0").toString()
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        var empresa: String = sharedPreferences.getString("empresa", "0").toString()

        getED(paramCVD(depto, puesto, idUser, empresa), this)
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuMCActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getED(params: paramCVD, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(CVCAPIService::class.java)
                .getDaysDepto("api/getDaysDepto.php/", params)
            val resp: CVCResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                val empleados = resp?.empleados ?: emptyList()
               // Log.i("gonzalito","$empleados")
                // Log.i("gonzalito","$err")
                if (!err) {
                    arrayEmp=empleados.toMutableList()
                    if (arrayEmp.isNotEmpty()) {
                        runOnUiThread {
                            arrayEmp.forEach { empleado ->

                                 var empleado2= Gson().toJson(empleado)
                                Log.i("gonzalito", "$empleado")
                                val empleado3: CVCItemResponse =
                                    Gson().fromJson(empleado2.toString(), CVCItemResponse::class.java)
                                // Crear una nueva fila
                                val newRow = TableRow(context)

                                // Crear las celdas de la fila
                                val nameTextView = TextView(context).apply {
                                    text = "${empleado3.nombre} ${empleado3.apellido_paterno} ${empleado3.apellido_materno.first()}"
                                    setPadding(8, 8, 0, 8)
                                    gravity = Gravity.LEFT
                                    setTextColor(Color.BLACK)
                                }

                                val totalesTextView = TextView(context).apply {
                                    text = empleado3.diasTotales
                                    setPadding(8, 8, 8, 8)
                                    gravity = Gravity.CENTER
                                    setTextColor(Color.BLACK)
                                }

                                val gozadosTextView = TextView(context).apply {
                                    text = empleado3.diasGozados
                                    setPadding(8, 8, 8, 8)
                                    gravity = Gravity.CENTER
                                    setTextColor(Color.BLACK)
                                }

                                val espeTextView = TextView(context).apply {
                                    text = empleado3.diasEspeciales
                                    setPadding(8, 8, 8, 8)
                                    gravity = Gravity.CENTER
                                    setTextColor(Color.BLACK)
                                }

// Agregar las celdas a la fila
                                newRow.addView(nameTextView)
                                newRow.addView(totalesTextView)
                                newRow.addView(gozadosTextView)
                                newRow.addView(espeTextView)

                                // Agregar la fila al TableLayout
                                Table.addView(newRow)
                            }
                        }
                    }

                } else {
                    runOnUiThread {
                        Toast.makeText(context, "No hay empleados", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                showError(context)
            }
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


    private fun showError(context: Context) {
        runOnUiThread {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        navigateToMenu()
        if (false) {
            super.onBackPressed()
        }
    }

}