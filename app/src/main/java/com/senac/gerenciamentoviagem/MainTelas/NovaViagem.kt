package com.senac.gerenciamentoviagem.MainTelas

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagem.Bd.AppDatabase
import com.senac.gerenciamentoviagem.Model.Tipo
import com.senac.gerenciamentoviagem.Model.Viagem
import com.senac.gerenciamentoviagem.ViewModel.Viagem.Factory.NovaViagemViewModelFactory
import com.senac.gerenciamentoviagem.ViewModel.Viagem.NovaViagemViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NovaViagem(
    userId: Int,
    viagemEditar: Viagem? = null,
    onNavigate: () -> Unit
) {
    val context = LocalContext.current


    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val viewModel: NovaViagemViewModel = viewModel(
        factory = NovaViagemViewModelFactory(db.viagemDao())
    )
    LaunchedEffect(viagemEditar) {
        viagemEditar?.let {
            viewModel.onDestinoChange(it.destino)
            viewModel.onTipoChange(it.tipo)
            viewModel.onDataInicioChange(it.dataInicio)
            viewModel.onDataFinalChange(it.dataFinal)
            viewModel.onOrcamentoChange(it.orcamento.toString())
        }
    }

    val destino by viewModel.destino.collectAsState()
    val tipo by viewModel.tipo.collectAsState()
    val dataInicio by viewModel.dataInicio.collectAsState()
    val dataFinal by viewModel.dataFinal.collectAsState()
    val orcamento by viewModel.orcamento.collectAsState()

    fun abrirDatePicker(onDateSelected: (LocalDateTime) -> Unit) {
        val now = LocalDateTime.now()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val date = LocalDateTime.of(year, month + 1, day, 0, 0)
                onDateSelected(date)
            },
            now.year,
            now.monthValue - 1,
            now.dayOfMonth
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = destino,
            onValueChange = { viewModel.onDestinoChange(it) },
            label = { Text("Destino") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Tipo de viagem")

        Row {
            RadioButton(
                selected = tipo == Tipo.Lazer,
                onClick = { viewModel.onTipoChange(Tipo.Lazer) }
            )
            Text("Lazer")

            RadioButton(
                selected = tipo == Tipo.Negocio,
                onClick = { viewModel.onTipoChange(Tipo.Negocio) }
            )
            Text("Negócio")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            abrirDatePicker { viewModel.onDataInicioChange(it.format(formatter).toString()) }
        }) {
            Text("Data Início: ${dataInicio}")
        }

        Button(onClick = {
            abrirDatePicker { viewModel.onDataFinalChange(it.format(formatter).toString()) }
        }) {
            Text("Data Final: ${dataFinal}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = orcamento,
            onValueChange = { viewModel.onOrcamentoChange(it) },
            label = { Text("Orçamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {

                if (viagemEditar == null) {

                    viewModel.salvar(userId) {
                        Toast.makeText(context, "Viagem salva!", Toast.LENGTH_SHORT).show()
                        onNavigate()
                    }

                } else {

                    viewModel.atualizar(
                        viagemEditar.copy(
                            destino = destino,
                            tipo = tipo,
                            dataInicio = dataInicio,
                            dataFinal = dataFinal,
                            orcamento = orcamento.toFloatOrNull() ?: 0f
                        )
                    ) {
                        Toast.makeText(context, "Viagem atualizada!", Toast.LENGTH_SHORT).show()
                        onNavigate()
                    }
                }

            }) {
                Text("Salvar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onNavigate() }) {
                Text("Cancelar")
            }
        }
    }
}