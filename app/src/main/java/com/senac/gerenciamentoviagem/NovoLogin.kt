package com.senac.gerenciamentoviagem

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagem.Bd.AppDatabase
import com.senac.gerenciamentoviagem.ViewModel.NovoLoginViewModel
import com.senac.gerenciamentoviagem.ViewModel.NovoLoginViewModelFactory

@Composable
fun NovoLogin(
    onNavigate: () -> Unit
) {
    val context = LocalContext.current

    val db = remember {
        AppDatabase.getDatabase(context)
    }

    val viewModel: NovoLoginViewModel = viewModel(
        factory = NovoLoginViewModelFactory(db.userDao())
    )

    val nome = viewModel.nome.collectAsState().value
    val email = viewModel.email.collectAsState().value
    val telefone = viewModel.telefone.collectAsState().value
    val senha = viewModel.senha.collectAsState().value
    val confirmarSenha = viewModel.confirmarSenha.collectAsState().value

    val senhaMatch = viewModel.senhaMatch.collectAsState(initial = true).value
    val isFormValid = viewModel.isFormValid.collectAsState(initial = false).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(nome, { viewModel.onNomeChange(it) }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { viewModel.onEmailChange(it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefone, { viewModel.onTelefoneChange(it) }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth())

        PasswordTextField(senha, { viewModel.onSenhaChange(it) }, "Senha")
        PasswordTextField(confirmarSenha, { viewModel.onConfirmarSenhaChange(it) }, "Confirmar Senha")

        if (!senhaMatch) {
            Text("As senhas não coincidem", color = Color.Red)
        }

        Button(
            onClick = {
                viewModel.cadastrar {

                    Toast.makeText(
                        context,
                        "Usuário cadastrado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    onNavigate()
                }
            },
            enabled = isFormValid
        ) {
            Text("Cadastrar")
        }
    }
}