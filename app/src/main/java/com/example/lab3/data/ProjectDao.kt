package com.example.lab3.data

import androidx.room.*
import com.example.lab3.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: String): Flow<Project?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProject(project: Project)

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProject(project: Project)
}