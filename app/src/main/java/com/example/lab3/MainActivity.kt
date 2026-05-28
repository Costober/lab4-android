package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab3.ui.theme.Lab3Theme
import com.example.lab3.ui.theme.screens.ProjectAppNavigation
import com.example.lab3.viewmodel.ProjectViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                // Отримуємо екземпляр ViewModel
                val viewModel: ProjectViewModel = viewModel()
                // Запускаємо навігацію
                ProjectAppNavigation(viewModel = viewModel)
            }
        }
    }
}