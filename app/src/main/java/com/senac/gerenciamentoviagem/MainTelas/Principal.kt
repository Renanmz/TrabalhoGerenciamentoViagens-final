package com.senac.gerenciamentoviagem.MainTelas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Principal(email: String, onNavigate: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nova Viagem",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Ação para Nova Viagem
                                scope.launch { drawerState.close() }
                            }
                    )
                    Text(
                        text = "Minhas Viagens",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Ação para Minhas Viagens
                                scope.launch { drawerState.close() }
                            }
                    )
                    Text(
                        text = "Sobre",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Ação para Sobre
                                scope.launch { drawerState.close() }
                            }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                UILabTopBar(onMenuClick = {
                    scope.launch { drawerState.open() }
                })
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Button(onClick = { onNavigate() }) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UILabTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Boa Viagem") },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}