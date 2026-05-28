package com.example.lab3.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab3.model.Project
import com.example.lab3.viewmodel.ProjectViewModel
import kotlin.math.roundToInt

// Кольори для відповідності макету
private val BlueAccent = Color(0xFF2196F3)
private val GreenProgress = Color(0xFF4CAF50)
private val BackgroundGray = Color(0xFFF5F5F5)

@Composable
fun ProjectAppNavigation(viewModel: ProjectViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "projects_list") {
        composable("projects_list") {
            ProjectsListScreen(navController, viewModel)
        }
        composable("add_project") {
            AddProjectScreen(navController, viewModel)
        }
        composable("details/{projectId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("projectId")
            DetailsProjectScreen(navController, viewModel, id)
        }
    }
}

// --- Екран 1: Список ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsListScreen(navController: NavController, viewModel: ProjectViewModel) {
    val projects by viewModel.projects.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої проекти", color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlueAccent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_project") }, containerColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        containerColor = BackgroundGray
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(projects) { project ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("details/${project.id}") },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(project.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(project.description, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { project.progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = GreenProgress
                        )
                    }
                }
            }
        }
    }
}

// --- Екран 2: Додавання ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(navController: NavController, viewModel: ProjectViewModel) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новий проект", color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlueAccent)
            )
        },
        containerColor = BackgroundGray
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Назва проекту") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Опис") }, modifier = Modifier.fillMaxWidth().height(120.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.addProject(name, desc); navController.popBackStack() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black), border = ButtonDefaults.outlinedButtonBorder) {
                Text("Створити")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black), border = ButtonDefaults.outlinedButtonBorder) {
                Text("Скасувати")
            }
        }
    }
}

// --- Екран 3: Деталі ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsProjectScreen(navController: NavController, viewModel: ProjectViewModel, id: String?) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.id == id }

    if (project == null) {
        return
    }

    var sliderPosition by remember(project.id) { mutableFloatStateOf(project.progress) }

    // Стан для відображення діалогового вікна редагування
    var showEditDialog by remember { mutableStateOf(false) }

    // Локальні змінні для збереження тексту редагування
    var editName by remember(project.id) { mutableStateOf(project.name) }
    var editDescription by remember(project.id) { mutableStateOf(project.description) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Деталі проекту", color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlueAccent)
            )
        },
        containerColor = BackgroundGray
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(project.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(project.description, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Прогрес", color = Color.Gray)
                    Text("${(sliderPosition * 100).roundToInt()}%", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = sliderPosition,
                        onValueChange = { newProgress ->
                            sliderPosition = newProgress
                            viewModel.updateProgress(project, newProgress)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Кнопка відкриває діалогове вікно редагування
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("Редагувати")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.deleteProject(project)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("Видалити")
            }
        }
    }

    // Компонент AlertDialog для зміни назви та опису
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Редагувати проект", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Назва проекту") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Опис") },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank()) {
                            viewModel.updateProjectDetails(project, editName, editDescription)
                            showEditDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent, contentColor = Color.White)
                ) {
                    Text("Зберегти")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDialog = false }) {
                    Text("Скасувати", color = Color.Black)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }
}