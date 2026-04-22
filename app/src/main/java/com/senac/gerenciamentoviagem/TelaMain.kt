package com.senac.gerenciamentoviagem

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagem.ViewModel.LoginViewModel

@Composable
fun TelaMain(
    onLogin: (String) -> Unit,
    onCadastro: () -> Unit,
    onRecuperarSenha: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val email = viewModel.email.collectAsState().value
    val senha = viewModel.senha.collectAsState().value
    val isEnabled = viewModel.isLoginEnabled.collectAsState(initial = false).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aviao),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") }
        )
        PasswordTextField(label = "Senha",
            senha = senha,
            onSenhaChange = { viewModel.onSenhaChange(it) })

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onLogin(email) },
                enabled = isEnabled) {
                Text(text = "Login")
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Novo usuário",
                color = Color.Blue,
                modifier = Modifier.clickable{ onCadastro() })
            Text(text = "Esqueci minha Senha",
                color = Color.Blue,
                modifier = Modifier.clickable{ onRecuperarSenha() })
        }
    }
}