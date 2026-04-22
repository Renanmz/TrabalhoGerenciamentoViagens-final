package com.senac.gerenciamentoviagem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagem.ViewModel.EsqueciSenhaViewModel

@Composable
fun EsqueciSenha(
    onNavigate: () -> Unit,
    viewModel: EsqueciSenhaViewModel = viewModel()
) {
    val email = viewModel.email.collectAsState().value
    val isValid = viewModel.isEmailValid.collectAsState(initial = false).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            isError = email.isNotBlank() && !isValid
        )

        // mensagem de erro
        if (email.isNotBlank() && !isValid) {
            Text(
                text = "Digite um email válido",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onNavigate,
                enabled = isValid
            ) {
                Text(text = "Mandar Senha")
            }
        }
    }
}