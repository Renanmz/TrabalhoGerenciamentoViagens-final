package com.senac.gerenciamentoviagem.MainTelas.Principal

import android.Manifest
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.senac.gerenciamentoviagem.Mapa.MapaViagem
import com.senac.gerenciamentoviagem.Model.Viagem
import com.senac.gerenciamentoviagem.ViewModel.PrincipalViewModel
import com.senac.gerenciamentoviagem.ViewModel.abrirMapa
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Principal(
    email: String,
    onNavigate: () -> Unit,
    onNovaViagem: () -> Unit,
    onMinhasViagens: () -> Unit,
    viewModel: PrincipalViewModel
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    /*
    =========================
    PERMISSÃO DE LOCALIZAÇÃO
    =========================
     */

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val granted = permissions.values.all { it }

        if (granted) {
            viewModel.buscarViagemAtual(context)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /*
    =========================
    DRAWER
    =========================
     */

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
                                onNovaViagem()
                            }
                    )

                    Text(
                        text = "Minhas Viagens",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onMinhasViagens()
                            }
                    )

                    Text(
                        text = "Sobre",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                    )
                }
            }
        }
    ) {

        /*
        =========================
        SCAFFOLD
        =========================
         */

        Scaffold(
            topBar = {
                UILabTopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()

        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                /*
                =========================
                LOCALIZAÇÃO
                =========================
                 */

                Text(
                    text = "Cidade atual: ${uiState.cidadeAtual ?: "Localizando..."}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                /*
                =========================
                VIAGEM ATUAL
                =========================
                 */

                uiState.viagemAtual?.let { viagem ->

                    Card(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Viagem Atual",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Destino: ${viagem.destino}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Data início: ${viagem.dataInicio}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Data final: ${viagem.dataFinal}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Tipo: ${viagem.tipo}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Orçamento: R$ ${viagem.orcamento}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Total de gastos: R$ ${viagem.totalGastos}")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.viagemAtual?.let { viagem ->

                        val context = LocalContext.current

                        var coordenadas by remember { mutableStateOf<LatLng?>(null) }

                        // ⚠️ roda só quando o destino mudar
                        LaunchedEffect(viagem.destino) {

                            val geocoder = Geocoder(context)

                            val result = geocoder.getFromLocationName(viagem.destino, 1)

                            val location = result?.firstOrNull()

                            coordenadas = if (location != null) {
                                LatLng(location.latitude, location.longitude)
                            } else {
                                null
                            }
                        }

                        coordenadas?.let { latLng ->

                            MapaViagem(
                                latitude = latLng.latitude,
                                longitude = latLng.longitude,
                                titulo = viagem.destino
                            )
                        }
                    }

                } ?: run {

                    Text(
                        text = "Nenhuma viagem encontrada para sua localização atual.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }



                Spacer(modifier = Modifier.height(24.dp))


                /*
                =========================
                BOTÃO CANCELAR
                =========================
                 */

                Button(
                    onClick = {
                        onNavigate()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UILabTopBar(
    onMenuClick: () -> Unit
) {

    TopAppBar(
        title = {
            Text(text = "Boa Viagem")
        },

        navigationIcon = {

            IconButton(
                onClick = {
                    onMenuClick()
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

