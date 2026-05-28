package com.example.lab3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lab3.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProjectViewModel : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    init {
        // Тестові дані як на макеті
        _projects.value = listOf(
            Project(name = "Мобільний додаток", description = "iOS/Android", progress = 0.75f),
            Project(name = "Веб-сайт", description = "Корпоративний", progress = 0.40f),
            Project(name = "База даних", description = "Дизайн та реалізація", progress = 0.90f)
        )
    }

    fun addProject(name: String, description: String) {
        val currentList = _projects.value.toMutableList()
        currentList.add(Project(name = name, description = description))
        _projects.value = currentList
    }

    fun deleteProject(id: String) {
        _projects.value = _projects.value.filter { it.id != id }
    }

    fun updateProgress(id: String, newProgress: Float) {
        _projects.value = _projects.value.map {
            if (it.id == id) it.copy(progress = newProgress) else it
        }
    }

    fun getProjectById(id: String): Project? {
        return _projects.value.find { it.id == id }
    }
}