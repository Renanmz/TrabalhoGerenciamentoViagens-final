package com.senac.gerenciamentoviagem.MainTelas.Roteiro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoteiroTela(viagemDestino: String, viewModel: RoteiroViewModel, onBack: () -> Unit) {
    var interesses by remember { mutableStateOf("") }
    val viagem = viewModel.viagemAtual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roteiro IA") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            
            Text("Destino: $viagemDestino", style = MaterialTheme.typography.headlineSmall)
            
            viagem?.let {
                Text("Tipo: ${it.tipo}", style = MaterialTheme.typography.bodyLarge)
                Text("Duração: ${viewModel.diasViagem} dias", style = MaterialTheme.typography.bodyLarge)
                Text("Orçamento: R$ ${it.orcamento}", style = MaterialTheme.typography.bodyLarge)
            }

            OutlinedTextField(
                value = interesses,
                onValueChange = { interesses = it },
                label = { Text("O que você gosta? (ex: Museus, Gastronomia)") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    viewModel.gerarRoteiro(interesses)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.carregando && viagem != null
            ) {
                if (viewModel.carregando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Gerar Roteiro Personalizado")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }

            if (viewModel.roteiroGerado.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    SelectionContainer {
                        Text(
                            text = viewModel.roteiroGerado,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
                // Espaço extra ao final para permitir rolar além do conteúdo
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
