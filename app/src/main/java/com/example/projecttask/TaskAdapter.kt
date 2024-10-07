package com.example.projecttask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val taskDate: TextView = itemView.findViewById(R.id.taskDate) // Asegúrate de que este ID exista en tu layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // Desconectar temporalmente el listener para evitar errores al reciclar vistas
        holder.taskCheckBox.setOnCheckedChangeListener(null)

        // Actualizar el contenido de la vista
        holder.taskCheckBox.text = task.title
        holder.taskCheckBox.isChecked = task.isCompleted

        // Mostrar fecha y hora en el TextView
        holder.taskDate.text = "${task.date} ${task.time}"

        // Configura el clic en la tarea
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // Función para añadir tarea
    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }

    // Función para actualizar tarea
    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasks[index] = updatedTask
            notifyItemChanged(index)
        }
    }

    // Función para eliminar tarea
    fun removeTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    // Función para eliminar todas las tareas completadas
    fun removeAllCompletedTasks() {
        tasks.removeAll { it.isCompleted }
        notifyDataSetChanged() // Notificar que la lista ha cambiado
    }

    // Función para obtener tareas completadas
    fun getCompletedTasks(): List<Task> {
        return tasks.filter { it.isCompleted }
    }
}
