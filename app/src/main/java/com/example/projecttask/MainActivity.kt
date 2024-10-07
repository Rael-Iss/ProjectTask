package com.example.projecttask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private var taskIdCounter = 0

    companion object {
        private const val REQUEST_CODE_ADD_TASK = 1
        private const val REQUEST_CODE_EDIT_TASK = 2
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate called")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val addTaskButton = findViewById<Button>(R.id.btnAddTask)
        val completedTasksButton = findViewById<Button>(R.id.btnCompleteTasks)

        Log.d(
            "MainActivity",
            "Views initialized: recyclerView: $recyclerView, addTaskButton: $addTaskButton, completedTasksButton: $completedTasksButton"
        )

        // Inicializar el adaptador y el RecyclerView
        taskAdapter = TaskAdapter(taskList) { task ->
            showTaskOptions(task)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Acción para crear una nueva tarea
        addTaskButton.setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK) // Código para nueva tarea
        }

        // Acción para mostrar tareas completadas
        completedTasksButton.setOnClickListener {
            showCompletedTasks()
        }
    }

    private fun showTaskOptions(task: Task) {
        // Crea un diálogo para mostrar opciones
        val options = arrayOf("Editar tarea", "Borrar tarea")
        AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Editar tarea
                        val intent = Intent(this, EditTaskActivity::class.java).apply {
                            putExtra("task_id", task.id)
                            putExtra("task_title", task.title)
                            putExtra("task_date", task.date)
                            putExtra("task_time", task.time)
                        }
                        startActivityForResult(
                            intent,
                            REQUEST_CODE_EDIT_TASK
                        ) // Código para editar tarea
                    }

                    1 -> {
                        // Borrar tarea
                        val position = taskList.indexOf(task)
                        if (position >= 0) { // Asegurarse de que la posición es válida
                            taskAdapter.removeTask(position)
                            Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .show()
    }

    private fun showCompletedTasks() {
        val completedTasks = taskAdapter.getCompletedTasks()
        if (completedTasks.isEmpty()) {
            Toast.makeText(this, "No hay tareas completadas", Toast.LENGTH_SHORT).show()
            return
        }

        val completedTasksTitles = completedTasks.map { it.title }
        AlertDialog.Builder(this)
            .setTitle("Tareas Completadas")
            .setItems(completedTasksTitles.toTypedArray()) { dialog, which ->
                // Puedes añadir la funcionalidad que desees al hacer clic en una tarea completada.
                val completedTask = completedTasks[which]
                showCompletedTaskOptions(completedTask)
            }
            .setNeutralButton("Borrar Todas las Tareas Completadas") { dialog, which ->
                // Eliminar todas las tareas completadas
                removeAllCompletedTasks()
            }
            .show()
    }

    private fun removeAllCompletedTasks() {
        // Eliminar todas las tareas completadas
        val completedTasks = taskAdapter.getCompletedTasks()
        completedTasks.forEach { task ->
            val position = taskList.indexOf(task)
            if (position >= 0) {
                taskAdapter.removeTask(position)
            }
        }
        Toast.makeText(this, "Todas las tareas completadas han sido eliminadas", Toast.LENGTH_SHORT).show()
    }

    private fun showCompletedTaskOptions(task: Task) {
        val options = arrayOf("Borrar tarea completada")
        AlertDialog.Builder(this)
            .setTitle("Opciones de Tarea Completada")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val position = taskList.indexOf(task)
                        if (position >= 0) { // Verificar si la tarea existe
                            taskAdapter.removeTask(position)
                            Toast.makeText(this, "Tarea completada eliminada", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val taskTitle = data.getStringExtra("task_title") ?: return
            val taskDate = data.getStringExtra("task_date") ?: ""
            val taskTime = data.getStringExtra("task_time") ?: ""

            if (requestCode == REQUEST_CODE_ADD_TASK) {
                // Nueva tarea
                val newTask =
                    Task(id = taskIdCounter++, title = taskTitle, date = taskDate, time = taskTime)
                taskAdapter.addTask(newTask)
                Toast.makeText(this, "Tarea añadida", Toast.LENGTH_SHORT).show()
            } else if (requestCode == REQUEST_CODE_EDIT_TASK) {
                // Editar tarea existente
                val taskId = data.getIntExtra("task_id", -1)
                if (taskId != -1) { // Asegúrate de que el ID de la tarea es válido
                    val updatedTask =
                        Task(id = taskId, title = taskTitle, date = taskDate, time = taskTime)
                    taskAdapter.updateTask(updatedTask)
                    Toast.makeText(this, "Tarea editada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al editar la tarea", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
