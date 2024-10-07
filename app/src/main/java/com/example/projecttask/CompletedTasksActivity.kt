package com.example.projecttask

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompletedTasksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var completedTasks: MutableList<Task> // Lista de tareas completadas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_tasks) // Asegúrate de tener este layout

        recyclerView = findViewById(R.id.recyclerView) // Asegúrate de tener este ID en tu layout
        val deleteCompletedButton: Button = findViewById(R.id.deleteCompletedButton) // Asegúrate de tener este ID en tu layout

        // Inicializa la lista de tareas completadas
        completedTasks = getCompletedTasksFromPreviousActivity() // Método que debes implementar para obtener las tareas

        // Configura el RecyclerView
        taskAdapter = TaskAdapter(completedTasks) { task ->
            // Aquí puedes manejar el clic en una tarea si es necesario
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Configura el botón para eliminar tareas completadas
        deleteCompletedButton.setOnClickListener {
            taskAdapter.removeAllCompletedTasks()
        }
    }

    private fun getCompletedTasksFromPreviousActivity(): MutableList<Task> {
        // Aquí puedes implementar la lógica para obtener las tareas completadas desde la actividad anterior
        // Por ejemplo, puedes pasar las tareas a través de un Intent o utilizar un ViewModel
        return mutableListOf() // Devuelve una lista de tareas completadas
    }
}
