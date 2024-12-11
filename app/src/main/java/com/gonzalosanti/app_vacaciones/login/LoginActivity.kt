package com.gonzalosanti.app_vacaciones.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.gonzalosanti.app_vacaciones.R
import com.gonzalosanti.app_vacaciones.login.cp.ChangePassActivity
import com.gonzalosanti.app_vacaciones.login.login.APIService
import com.gonzalosanti.app_vacaciones.login.login.HeaderInterceptor
import com.gonzalosanti.app_vacaciones.login.login.UserResponse
import com.gonzalosanti.app_vacaciones.login.login.paramLogin
import com.gonzalosanti.app_vacaciones.login.menu.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Build
import android.view.Display
import android.view.autofill.AutofillManager
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLSocketFactory
import kotlin.math.log

class LoginActivity : AppCompatActivity() {


    private lateinit var LoginName: AppCompatEditText
    private lateinit var LoginPassw: AppCompatEditText
    private lateinit var btnLogin: AppCompatImageButton
    private lateinit var loginErr: TextView
    private lateinit var containMain: ConstraintLayout



    private lateinit var autofillManager: AutofillManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        initComponents()


       // initUI()
        initListeners()
        //setupAuth()

    }

    private fun initUI() {
        var height = 0

        val windowManager = getSystemService(WindowManager::class.java)
        val display: Display? = windowManager?.defaultDisplay

        if (display != null) {
            val displayMetrics = DisplayMetrics()

            display.getMetrics(displayMetrics)

            // obtiene la altura de la pantalla asignada a la aplicación
            height = displayMetrics.heightPixels


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

    private fun searchByName(params: paramLogin, context: Context) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(APIService::class.java)
                    .getLoginByBreeds("api/login.php/", params)
                val respLogin = call.body()

                if (call.isSuccessful) {
                    validateLogin(respLogin)
                } else {
                    // Manejo de error cuando la respuesta es exitosa pero el código de estado no es 200
                    Log.i("gonzalio", "Respuesta no exitosa: ${call.code()}")
                    withContext(Dispatchers.Main) {
                        showError(context)
                    }
                }
            } catch (e: SocketTimeoutException) {
                // Manejo específico para excepciones de tiempo de espera
                Log.e("gonzalio", "Error de conexión: El servidor no respondió a tiempo.", e)
                withContext(Dispatchers.Main) {
                    showError(context)
                }
            } catch (e: IOException) {
                // Manejo para errores generales de entrada/salida
                Log.e("gonzalio", "Error de entrada/salida: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    showError(context)
                }
            } catch (e: Exception) {
                // Manejo para otros errores no previstos
                Log.e("gonzalio", "Error inesperado: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
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

    private fun initComponents() {
        LoginName = findViewById(R.id.LoginName)
        LoginPassw = findViewById(R.id.LoginPassw)
        btnLogin = findViewById(R.id.btnLogin)
        loginErr = findViewById(R.id.loginErr)
        containMain = findViewById(R.id.containMain)
        Log.i("gonzalito", "Se crearon")

        LoginName.setAutofillHints(View.AUTOFILL_HINT_USERNAME)
        LoginPassw.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
    }

    private fun initListeners() {
         btnLogin.setOnClickListener { login() }
        //btnLogin.setOnClickListener {
       //     authenticate {
        //        login(canAuthenticate)
       //     }
       // }
    }

    private fun login() {

            var user = LoginName.text.toString();
            var passw = LoginPassw.text.toString();

            searchByName(paramLogin(user, passw),this)
        

    }



    private fun validateLogin(respLogin: UserResponse?) {
        val err = respLogin?.err ?: true

        if (!err) {
            val idUser = respLogin?.id
            val nombre = respLogin?.nombre
            val apellidoP = respLogin?.apellidoPaterno
            val empresa = respLogin?.empresa
            val departamento = respLogin?.departamento
            val puesto = respLogin?.puesto
            val tipoUser = respLogin?.tipoUser
            val perfilUser = respLogin?.perfilUser
            val fechaingreso = respLogin?.fechaIngreso
            val changePass = respLogin?.changePass


            val sharedPreferences = getSharedPreferences("mi_app", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putString("nombre", nombre)
            editor.putString("idUser", idUser)
            editor.putString("apellidoP", apellidoP)
            editor.putString("empresa", empresa)
            editor.putString("departamento", departamento)
            editor.putString("puesto", puesto)
            editor.putString("tipoUser", tipoUser)
            editor.putString("perfilUser", perfilUser)
            editor.putString("fechaIngreso", fechaingreso)
            editor.apply()

            if (changePass === true) {
                val intent = Intent(this, ChangePassActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }

        } else {
            runOnUiThread {
                val causa = respLogin?.causa ?: "4"
                when (causa) {
                    "1" -> loginErr.text = "contraseña incorrecta"
                    "2" -> loginErr.text = "usuario incorrecto"
                    "3->" -> loginErr.text = "usuario inactivo"
                    else -> loginErr.text = "ocurrio un error"
                }
                loginErr.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                val delayMillis = 2000L // Retraso de 1 segundo (1000 milisegundos)

                handler.postDelayed({
                    loginErr.visibility = View.GONE
                }, delayMillis)
            }


        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Esto cerrará todas las actividades en la pila.
    }


}