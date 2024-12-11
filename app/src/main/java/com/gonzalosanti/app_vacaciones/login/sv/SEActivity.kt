package com.gonzalosanti.app_vacaciones.login.sv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.cv.CVAPIService
import com.gonzalosanti.app_vacaciones.login.cv.CVResponse
import com.gonzalosanti.app_vacaciones.login.cv.paramCV
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.gonzalosanti.app_vacaciones.login.menu.MenuSVActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class SEActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var EtDateStart: EditText
    private lateinit var EtDateEnd: EditText
    private lateinit var BtnSv: Button
    private lateinit var LYEnd: LinearLayoutCompat
    private lateinit var LYStart: LinearLayoutCompat
    private lateinit var LyNoDays: LinearLayout
    private lateinit var textMessage: TextView

    lateinit var dateStart: String
    lateinit var dateEnd: String
    lateinit var isActiveDateEnd: Any
    lateinit var isActiveDateStart: Any
    lateinit var daysAvailable: String
    lateinit var daysUsed: String
    lateinit var daysTotal: String
    lateinit var svBandResp: String
    lateinit var statusText:String

    lateinit var sharedPreferences: SharedPreferences
    lateinit var user: String


    companion object {
        const val DAYS_TOTAL = "DAYS_TOTAL"
        const val DAYS_USED = "DAYS_USED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        dateStart = ""
        dateEnd = ""
        isActiveDateEnd = false
        isActiveDateStart = true
        statusText="initStatusText"


        sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("idUser", "0").toString()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svactivity)

        initComponents()
        initUI()
        initListeners()
    }

    private fun initComponents() {
        BtnBack = findViewById(R.id.BtnBack)
        EtDateStart = findViewById(R.id.EtDateStart)
        EtDateEnd = findViewById(R.id.EtDateEnd)
        LYEnd = findViewById((R.id.LYEnd))
        LYStart = findViewById((R.id.LYStart))
        LyNoDays = findViewById((R.id.LyNoDays))
        BtnSv = findViewById((R.id.BtnSv))
        textMessage = findViewById((R.id.textMessage))
    }

    private fun initUI() {
        getDays(paramCV(user), this)
    }

    private fun initListeners() {
        EtDateStart.setOnClickListener { if (isActiveDateStart == true) showDataPickerDialog(1) }
        EtDateEnd.setOnClickListener { if (isActiveDateEnd == true) showDataPickerDialog(2) }
        BtnBack.setOnClickListener { navigateToMenu() }
        BtnSv.setOnClickListener {
            createSV(paramSV(user, dateStart, dateEnd), this)
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToMenuSV() {
        val intent = Intent(this, MenuSVActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRSV() {
        val intent = Intent(this, RSVActivity::class.java)
        intent.putExtra(DAYS_TOTAL, daysTotal)
        intent.putExtra(DAYS_USED, daysUsed)
        startActivity(intent)
        finish()
    }

    private fun showDataPickerDialog(band: Int) {
        val datePicker =
            DatePickerFragment(
                { day, month, year, band, daysAvailable, dateStart ->
                    onDateSelected(day, month, year, band, daysAvailable, dateStart)
                },
                band,
                daysAvailable,
                dateStart
            )
        datePicker.show(supportFragmentManager, "datePicker")

    }

    fun onDateSelected(
        day: Int,
        month: Int,
        year: Int,
        band: Int,
        daysAvailable: String,
        dateStart: String
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
            BtnSv.isEnabled = true
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

    private fun getDays(params: paramCV, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(CVAPIService::class.java)
                .getLoginByBreeds("api/getDays.php/", params)
            val resp: CVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                if (!err) {
                    var restantes: Float = resp!!.diasEspeciales.toFloat()- resp!!.pendEspecial.toFloat()
                    daysAvailable = restantes.toString()

                    if (daysAvailable.toFloat() <= 0) {
                        runOnUiThread {
                            isActiveDateStart = false
                            LYStart.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_fondo_gris
                                )
                            )
                            LyNoDays.visibility = View.VISIBLE
                        }
                    }

                }

            } else {
                showError(context)
            }
        }
    }

    private fun createSV(params: paramSV, context: Context) {

        CoroutineScope(Dispatchers.IO).launch {

            val call = getRetrofit().create(SVAPIService::class.java)
                .createSVacation("api/createSE.php/", params)
            val resp: SVResponse? = call.body()

            if (call.isSuccessful) {
                val err = resp?.err ?: true
                if (!err) {
                    runOnUiThread {
                        svBandResp = "true"
                        daysTotal = resp?.totalDays ?: "0"
                        daysUsed = resp?.countDays ?: "0"
                        statusText = resp?.statusText ?: "statusText"

                        textMessage.setBackgroundColor(ContextCompat.getColor(context, R.color.color_success))
                        textMessage.setText("Solicitud creada")
                        textMessage.visibility = View.VISIBLE
                        BtnSv.isEnabled = false
                        val handler = Handler(Looper.getMainLooper())
                        val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)

                        handler.postDelayed({
                            textMessage.visibility = View.GONE
                            navigateToMenu()
                        }, delayMillis)
                    }
                } else {
                    runOnUiThread {
                        svBandResp = "false"
                        statusText = resp?.statusText ?: "statusText"

                        textMessage.setBackgroundColor(ContextCompat.getColor(context, R.color.color_danger))
                        Log.i("statusText",statusText)
                        textMessage.setText(statusText)
                        textMessage.visibility = View.VISIBLE
                        BtnSv.isEnabled = false
                        val handler = Handler(Looper.getMainLooper())
                        val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)

                        handler.postDelayed({
                            textMessage.visibility = View.GONE
                            BtnSv.isEnabled = true
                            navigateToMenuSV()
                        }, delayMillis)
                    }
                }

            } else {
                showError(context)
            }


        }
    }

    private fun showError(context: Context) {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        navigateToMenu()
        if(false) {
            super.onBackPressed()
        }
    }
}