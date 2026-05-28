package com.example.lab3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab3.data.AppDatabase
import com.example.lab3.data.ProjectRepository
import com.example.lab3.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository
    val projects: StateFlow<List<Project>>

    init {
        val projectDao = AppDatabase.getDatabase(application).projectDao()
        repository = ProjectRepository(projectDao)
        projects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun getProjectById(id: String): StateFlow<Project?> {
        return repository.getProjectById(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    fun addProject(name: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(Project(name = name, description = description))
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(project)
        }
    }

    fun updateProgress(project: Project, newProgress: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(project.copy(progress = newProgress))
        }
    }
}