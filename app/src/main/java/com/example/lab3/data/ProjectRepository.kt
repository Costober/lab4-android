package com.example.lab3.data

import com.example.lab3.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    fun getProjectById(id: String): Flow<Project?> = projectDao.getProjectById(id)

    fun insert(project: Project) = projectDao.insertProject(project) // БЕЗ suspend

    fun update(project: Project) = projectDao.updateProject(project) // БЕЗ suspend

    fun delete(project: Project) = projectDao.deleteProject(project) // БЕЗ suspend
}