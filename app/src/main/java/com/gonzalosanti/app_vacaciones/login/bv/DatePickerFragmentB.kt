package com.gonzalosanti.app_vacaciones.login.bv

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.gonzalosanti.app_vacaciones.R
import java.util.Calendar

class DatePickerFragmentB (
    val listener: (day: Int, month: Int, year: Int, band: Int, dateStart: String) -> Unit,
    val band: Int, val dateStart: String
):
    DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year, band,  dateStart)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var picker: DatePickerDialog
        var c: Calendar = Calendar.getInstance()

        if (band == 1) {
            c.add(Calendar.DAY_OF_MONTH, 7)
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)

            picker = DatePickerDialog(
                activity as Context,
                R.style.DatePickerTheme, this, year, month, day
            )

            picker.datePicker.setOnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                if (dayOfWeek == Calendar.SUNDAY) {
                    // Si es domingo, mostrar un mensaje o hacer lo que necesites aquí
                    Toast.makeText(activity, "Los domingos están bloqueados", Toast.LENGTH_SHORT)
                        .show()
                    // Para evitar que se seleccione el día, puedes revertir la selección

                    datePicker.updateDate(year, month, day)
                }
            }


            picker.datePicker.minDate = c.timeInMillis
        } else {
            val partes = dateStart.split("-")
            val dia = partes[0].toInt()
            val mes = partes[1].toInt()
            val año = partes[2].toInt()

            c.set(Calendar.YEAR, año)
            c.set(
                Calendar.MONTH,
                mes-1
            ) // Ten en cuenta que en Calendar, los meses comienzan desde 0
            c.set(Calendar.DAY_OF_MONTH, dia)

            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)

            picker = DatePickerDialog(
                activity as Context,
                R.style.DatePickerTheme, this, year, month, day
            )

            picker.datePicker.setOnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                if (dayOfWeek == Calendar.SUNDAY) {
                    // Si es domingo, mostrar un mensaje o hacer lo que necesites aquí
                    Toast.makeText(activity, "Los domingos están bloqueados", Toast.LENGTH_SHORT)
                        .show()
                    // Para evitar que se seleccione el día, puedes revertir la selección

                    datePicker.updateDate(year, month, day)
                }
            }


            picker.datePicker.minDate = c.timeInMillis




        }

        return picker

    }

    }