package com.senac.gerenciamentoviagem.MainTelas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagem.Bd.AppDatabase
import com.senac.gerenciamentoviagem.Model.Viagem
import com.senac.gerenciamentoviagem.R
import com.senac.gerenciamentoviagem.ViewModel.Viagem.Factory.ViagemViewModelFactory
import com.senac.gerenciamentoviagem.ViewModel.Viagem.ViagemViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MinhasViagens(
    userId: Int,
    onNavigate: () -> Unit,
    onEditar: (Viagem) -> Unit = {}
) {
    val context = LocalContext.current

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val viewModel: ViagemViewModel = viewModel(
        factory = ViagemViewModelFactory(db.viagemDao())
    )


    val viagens by viewModel.viagens.collectAsState()

    LaunchedEffect(userId) {
        viewModel.carregar(userId)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Button(onClick = { onNavigate() }) {
            Text("Voltar")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(viagens) { viagem ->

                var showDialog by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .pointerInput(viagem.id) {

                            detectHorizontalDragGestures { _, dragAmount ->


                                if (dragAmount > 40 || dragAmount < -40) {
                                    showDialog = true
                                }
                            }
                        }
                        .combinedClickable(
                            onClick = {
                                // futuro: detalhes da viagem
                            },
                            onLongClick = {
                                onEditar(viagem)
                            }
                        )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val imagem = if (viagem.tipo.name == "Lazer") {
                            R.drawable.lazer
                        } else {
                            R.drawable.negocio
                        }

                        Image(
                            painter = painterResource(id = imagem),
                            contentDescription = "Tipo de viagem",
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text("Destino: ${viagem.destino}")
                            Text(
                                "Data: ${
                                    formatarData(viagem.dataInicio)
                                } - ${
                                    formatarData(viagem.dataFinal)
                                }"
                            )
                            Text("Orçamento: R$ ${viagem.orcamento}")
                        }
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmar exclusão") },
                        text = { Text("Deseja realmente excluir esta viagem?") },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.excluir(viagem, userId)
                                Toast.makeText(context, "Excluído", Toast.LENGTH_SHORT).show()
                                showDialog = false
                            }) {
                                Text("Sim")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showDialog = false
                            }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
fun formatarData(data: String): String {

    return try {

        val formatterBanco = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formatterTela = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        java.time.LocalDate
            .parse(data, formatterBanco)
            .format(formatterTela)

    } catch (e: Exception) {

        data
    }
}