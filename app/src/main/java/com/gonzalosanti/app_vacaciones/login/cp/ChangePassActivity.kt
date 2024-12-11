package com.gonzalosanti.app_vacaciones.login.cp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.LoginActivity
import com.gonzalosanti.app_vacaciones.login.avhistory.AvHistoryActivity
import com.gonzalosanti.app_vacaciones.login.hv.HVAPIService
import com.gonzalosanti.app_vacaciones.login.hv.HVResponse
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangePassActivity : AppCompatActivity() {
    private lateinit var BtnBack: ImageView
    private lateinit var BtnCp: Button
    private lateinit var newPassText: TextInputEditText
    private lateinit var confirmPassText: TextInputEditText
    private lateinit var textMessage: TextView

    private lateinit var newPass: String
    private lateinit var id_user: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        initComponents()
        initListeners()

    }

    private fun initComponents() {
        val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)
        id_user = sharedPreferences.getString("idUser", "0").toString()
        
        BtnBack = findViewById(R.id.BtnBack)
        BtnCp = findViewById(R.id.BtnCp)
        newPassText = findViewById(R.id.newPassText)
        confirmPassText = findViewById(R.id.confirmPassText)
        textMessage = findViewById(R.id.textMessage)
    }

    private fun initListeners() {
        BtnBack.setOnClickListener { navigateToLogin() }
        BtnCp.setOnClickListener { changePass() }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun changePass() {
        var pass1: String = newPassText.text.toString()
        var pass2: String = confirmPassText.text.toString()
        val handler = Handler(Looper.getMainLooper())
        val delayMillis = 1500L // Retraso de 1 segundo (1000 milisegundos)
        val contexto = this

        if (pass1.length == 0 || pass2.length == 0) {
            textMessage.text = "Las contraseñas no pueden estar vacias"
            textMessage.visibility = View.VISIBLE
            BtnCp.isEnabled = false

            handler.postDelayed({
                textMessage.visibility = View.GONE
                BtnCp.isEnabled = true
            }, delayMillis)
        } else {
            if (pass1 != pass2) {
                textMessage.text = "Las contraseñas no coinciden"
                textMessage.visibility = View.VISIBLE
                BtnCp.isEnabled = false

                handler.postDelayed({
                    textMessage.visibility = View.GONE
                    BtnCp.isEnabled = true
                }, delayMillis)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val call = getRetrofit().create(PASSAPIService::class.java)
                        .setPass("api/updatePass.php/", paramNewPass(id_user, pass2))
                    val resp: PassResponse? = call.body()

                    if (call.isSuccessful) {
                        val err = resp?.err ?: true
                        val statusText = resp?.statusText ?: "0"

                        if (!err) {
                            val intent = Intent(contexto, MenuActivity::class.java)
                            startActivity(intent)

                        } else {
                            showError(contexto)
                        }

                    } else {
                        showError(contexto)
                    }
                }
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

    private fun showError(contexto: Context) {
        Toast.makeText(contexto, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        navigateToLogin()
        if(false) {
            super.onBackPressed()
        }
    }
}

