package com.example.lab3.model

import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    var progress: Float = 0f
)