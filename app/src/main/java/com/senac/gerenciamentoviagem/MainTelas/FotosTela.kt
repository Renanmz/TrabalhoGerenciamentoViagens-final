package com.senac.gerenciamentoviagem.MainTelas

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.senac.gerenciamentoviagem.ViewModel.Viagem.FotoViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FotosTela(
    viagemId: Int,
    userId: Int,
    viewModel: FotoViewModel, // Injetando o ViewModel para persistência
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    // Observa a lista de fotos do banco de dados em tempo real
    val fotos by viewModel.fotos.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Launcher para selecionar da Galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Salva permanentemente no Banco de Dados
            viewModel.adicionarFoto(viagemId, it.toString())
        }
        showSheet = false
    }

    // Launcher para Capturar Foto da Câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                it,
                "Viagem_${viagemId}_${UUID.randomUUID()}",
                null
            )
            if (path != null) {
                // Salva o caminho da foto no Banco de Dados
                viewModel.adicionarFoto(viagemId, path)
            }
        }
        showSheet = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galeria da Viagem") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Foto")
            }
        }
    ) { padding ->

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Adicionar foto da...", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ButtonOption(
                            icon = Icons.Default.PhotoCamera,
                            label = "Câmera",
                            onClick = { cameraLauncher.launch() }
                        )

                        ButtonOption(
                            icon = Icons.Default.Image,
                            label = "Galeria",
                            onClick = { galleryLauncher.launch("image/*") }
                        )
                    }
                }
            }
        }

        if (fotos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma foto encontrada.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(fotos) { foto ->
                    Box(modifier = Modifier.padding(4.dp)) {
                        Card(
                            modifier = Modifier.aspectRatio(1f),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            AsyncImage(
                                model = foto.uri,
                                contentDescription = "Foto da viagem",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        // Botão para excluir a foto do banco de dados
                        IconButton(
                            onClick = { viewModel.deletarFoto(foto) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(32.dp))
        }
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}
