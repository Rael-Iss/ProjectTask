package com.example.projecttask

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var dateInput: Button
    private lateinit var timeInput: Button
    private lateinit var saveButton: Button
    private val calendar = Calendar.getInstance() // Para manejar fecha y hora

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        titleInput = findViewById(R.id.editTextTitle)
        dateInput = findViewById(R.id.btnSelectDate)
        timeInput = findViewById(R.id.btnSelectTime)
        saveButton = findViewById(R.id.btnSaveTask)

        // Si estamos editando una tarea, rellenar los campos
        val taskId = intent.getIntExtra("task_id", -1)
        val taskTitle = intent.getStringExtra("task_title")
        val taskDate = intent.getStringExtra("task_date")
        val taskTime = intent.getStringExtra("task_time")

        if (taskTitle != null) titleInput.setText(taskTitle)
        if (taskDate != null) dateInput.text = taskDate
        if (taskTime != null) timeInput.text = taskTime

        // Mostrar el DatePicker al hacer clic en el botón de fecha
        dateInput.setOnClickListener {
            showDatePicker()
        }

        // Mostrar el TimePicker al hacer clic en el botón de hora
        timeInput.setOnClickListener {
            showTimePicker()
        }

        saveButton.setOnClickListener {
            val resultIntent = intent.apply {
                putExtra("task_id", taskId)
                putExtra("task_title", titleInput.text.toString())
                putExtra("task_date", dateInput.text.toString())
                putExtra("task_time", timeInput.text.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Volver a MainActivity
        }
    }

    // Mostrar DatePicker
    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInput() // Actualizar el botón con la fecha seleccionada
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Mostrar TimePicker
    private fun showTimePicker() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInput() // Actualizar el botón con la hora seleccionada
        }

        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24 horas
        ).show()
    }

    // Actualizar el campo de fecha con el formato seleccionado
    private fun updateDateInput() {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.text = format.format(calendar.time)
    }

    // Actualizar el campo de hora con el formato seleccionado
    private fun updateTimeInput() {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeInput.text = format.format(calendar.time)
    }
}
