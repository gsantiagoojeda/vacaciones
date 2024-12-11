package com.gonzalosanti.app_vacaciones.login.bv

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.avhistory.AvHistoryActivity
import com.gonzalosanti.app_vacaciones.login.hv.HVAPIService
import com.gonzalosanti.app_vacaciones.login.hv.HVResponse
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.gonzalosanti.app_vacaciones.login.sv.DatePickerFragment
import com.gonzalosanti.app_vacaciones.login.sv.SVAPIService
import com.gonzalosanti.app_vacaciones.login.sv.SVResponse
import com.gonzalosanti.app_vacaciones.login.sv.paramSV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class BvActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var cardSelected:CardView
    private lateinit var tVEmp:TextView
    private lateinit var tVEmpList:TextView
    private lateinit var BtnHistory: ImageView

    private lateinit var EtDateStart: EditText
    private lateinit var EtDateEnd: EditText
    private lateinit var BtnBD: Button
    private lateinit var LYEnd: LinearLayoutCompat
    private lateinit var LYStart: LinearLayoutCompat

    lateinit var dateStart: String
    lateinit var dateEnd: String
    lateinit var isActiveDateEnd: Any
    lateinit var isActiveDateStart: Any
    lateinit var statusText:String
    private lateinit var textMessage: TextView

    lateinit var selectedEmp: BooleanArray
    val empList: ArrayList<Int> = ArrayList()
    //val empArray: ArrayList<String> = ArrayList()
    var empArray: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        dateStart = ""
        dateEnd = ""
        isActiveDateEnd = false
        isActiveDateStart = true

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_bv)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initUI()
        initListeners()
    }

    private fun initComponents(){
        cardSelected= findViewById(R.id.EmpleadoSelect)
        tVEmp= findViewById(R.id.TVEmp)
        tVEmpList= findViewById(R.id.TvEmpList)
        BtnBack = findViewById(R.id.BtnBack)
        BtnHistory = findViewById(R.id.BtnHistory)

        EtDateStart = findViewById(R.id.EtDateStart)
        EtDateEnd = findViewById(R.id.EtDateEnd)
        LYEnd = findViewById((R.id.LYEnd))
        LYStart = findViewById((R.id.LYStart))
        BtnBD = findViewById((R.id.BtnBD))
        textMessage = findViewById((R.id.textMessage))
    }

    private  fun initUI(){
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var depto: String = sharedPreferences.getString("departamento", "0").toString()
        var puesto: String = sharedPreferences.getString("puesto", "0").toString()
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        var empresa: String = sharedPreferences.getString("empresa", "0").toString()
        Log.i("gonzalito", "yo $depto , $puesto , $idUser , $empresa")
        getED(paramED(depto,puesto,idUser,empresa), this)

    }

    private  fun  initListeners(){
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        var idUser: String = sharedPreferences.getString("idUser", "0").toString()
        BtnBack.setOnClickListener { navigateToMenu() }
        cardSelected.setOnClickListener{
            showEmpDialog()
        }
        BtnHistory.setOnClickListener { navigateToHistory() }

        EtDateStart.setOnClickListener { if (isActiveDateStart == true) showDataPickerDialog(1) }
        EtDateEnd.setOnClickListener { if (isActiveDateEnd == true) showDataPickerDialog(2) }
        BtnBD.setOnClickListener {
            val resultEmp = empList.map { empArray[it] }
            //Log.i("gonzalito","$resultEmp")
            createBD(paramBD(idUser, dateStart, dateEnd, resultEmp), this)
        }
    }

    private fun showEmpDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("selecciona a los empleados")
            .setCancelable(false)
            .setMultiChoiceItems(empArray.toTypedArray(), selectedEmp) { dialog, which, isChecked ->
                if (isChecked) {
                    empList.add(which)
                } else {
                    empList.remove(which)
                }
            }
            .setPositiveButton("Aceptar") { dialog, which ->
                val stringBuilder = StringBuilder()
                for (i in empList.indices) {
                    stringBuilder.append(empArray[empList[i]])
                    if (i != empList.size - 1) {
                        stringBuilder.append("\n")
                    }
                }
                tVEmpList.text = stringBuilder.toString()
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .setNeutralButton("Limpiar") { dialog, which ->
                for (i in selectedEmp.indices) {
                    selectedEmp[i] = false
                }
                empList.clear()
                tVEmpList.text = ""
            }
        builder.show()
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

    private fun getED(params: paramED, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(EDAPIService::class.java)
                .getEmpleados("api/getEmpDepto.php/", params)
            val resp: EDResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                val empleados = resp?.empleados ?: emptyList()

                if (!err) {
                    // Aquí ya tenemos una lista de objetos Empleado
                    empArray =
                        empleados.map { "${it.id} - ${it.nombre} ${it.apellido_paterno}" }.toMutableList() as ArrayList<String> // Si solo quieres los nombres
                    selectedEmp = BooleanArray(empArray.size) // Ahora puedes inicializar selectedEmp
                  //  Log.i("gonzalito", "llenando: $empleados")
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


    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHistory() {
        val intent = Intent(this, HBDActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(context: Context) {
        runOnUiThread {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDataPickerDialog(band: Int) {
        val datePicker =
            DatePickerFragmentB(
                { day, month, year, band, dateStart ->
                    onDateSelected(day, month, year, band)
                },
                band,
                dateStart
            )
        datePicker.show(supportFragmentManager, "datePicker")

    }

    fun onDateSelected(
        day: Int,
        month: Int,
        year: Int,
        band: Int
    ) {
        val fechaOriginal = "${day}-${month + 1}-${year}"
        val formatoOriginal = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
        val fecha = formatoOriginal.parse(fechaOriginal)

        val formatoDeseado = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoDeseado.format(fecha!!)
        if (band == 1) {
            EtDateStart.setText(fechaFormateada)
            this.dateStart = fechaOriginal
            isActiveDateEnd = true
            LYEnd.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        } else {
            dateEnd = fechaOriginal
            EtDateEnd.setText(fechaFormateada)
            BtnBD.isEnabled = true
        }

    }

    private fun createBD(params: paramBD, context: Context) {

        CoroutineScope(Dispatchers.IO).launch {

            val call = getRetrofit().create(BDAPIService::class.java)
                .setBD("api/createBD.php/", params)
            val resp: BDResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                if (!err) {
                    runOnUiThread {
                        statusText = resp?.statusText ?: "statusText"

                        textMessage.setBackgroundColor(ContextCompat.getColor(context, R.color.color_success))
                        textMessage.setText("Solicitud creada")
                        textMessage.visibility = View.VISIBLE
                        BtnBD.isEnabled = false
                        val handler = Handler(Looper.getMainLooper())
                        val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)

                        handler.postDelayed({
                            textMessage.visibility = View.GONE
                            navigateToMenu()
                        }, delayMillis)
                    }
                } else {
                    runOnUiThread {
                        statusText = resp?.statusText ?: "statusText"

                        textMessage.setBackgroundColor(ContextCompat.getColor(context, R.color.color_danger))
                        Log.i("statusText",statusText)
                        textMessage.setText(statusText)
                        textMessage.visibility = View.VISIBLE
                        BtnBD.isEnabled = false
                        val handler = Handler(Looper.getMainLooper())
                        val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)

                        handler.postDelayed({
                            textMessage.visibility = View.GONE
                            navigateToMenu()
                        }, delayMillis)

                    }
                }

            } else {
                showError(context)
            }


        }
    }

    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }

}


data class Empleado(
    val id: String,
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val departamento: String
)