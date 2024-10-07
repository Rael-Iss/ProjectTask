package com.example.projecttask

data class Task(
    var id: Int,
    var title: String,
    var isCompleted: Boolean = false,
    var date: String = "",
    var time: String = ""
)