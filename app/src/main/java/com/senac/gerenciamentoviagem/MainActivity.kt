package com.senac.gerenciamentoviagem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.senac.gerenciamentoviagem.EntradaTelas.EsqueciSenha
import com.senac.gerenciamentoviagem.EntradaTelas.NovoLogin
import com.senac.gerenciamentoviagem.EntradaTelas.TelaMain
import com.senac.gerenciamentoviagem.MainTelas.MinhasViagens
import com.senac.gerenciamentoviagem.MainTelas.NovaViagem
import com.senac.gerenciamentoviagem.MainTelas.Principal
import com.senac.gerenciamentoviagem.Rotas.RouteEsqueciSenha
import com.senac.gerenciamentoviagem.Rotas.RouteMain
import com.senac.gerenciamentoviagem.Rotas.RouteMinhasViagens
import com.senac.gerenciamentoviagem.Rotas.RouteNovaViagem
import com.senac.gerenciamentoviagem.Rotas.RouteNovoLogin
import com.senac.gerenciamentoviagem.Rotas.RoutePrincipal
import com.senac.gerenciamentoviagem.ui.theme.GerenciamentoViagemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GerenciamentoViagemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val backStack = rememberNavBackStack(RouteMain) //primeira rota

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is RouteMain -> NavEntry(key) {
                    TelaMain(
                        onLogin = { userId, email ->
                            backStack.add(RoutePrincipal(userId, email))
                        },
                        onCadastro = {
                            backStack.add(RouteNovoLogin)
                        },
                        onRecuperarSenha = {
                            backStack.add(RouteEsqueciSenha)
                        }
                    )
                }

                is RoutePrincipal -> NavEntry(key) {
                    Principal(email = key.email,
                        onNavigate = {
                        backStack.removeLastOrNull()
                        },
                        onNovaViagem = {
                            backStack.add(RouteNovaViagem(key.userId))
                        },
                        onMinhasViagens = {
                            backStack.add(RouteMinhasViagens(key.userId))
                        }
                    )
                }

                is RouteNovoLogin -> NavEntry(key) {
                    NovoLogin(onNavigate = {
                        backStack.removeLastOrNull()
                    })
                }

                is RouteEsqueciSenha -> NavEntry(key) {
                    EsqueciSenha(onNavigate = {
                        backStack.removeLastOrNull()
                    })
                }

                is RouteNovaViagem -> NavEntry(key) {
                    NovaViagem(
                        userId = key.userId,
                        onNavigate = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is RouteMinhasViagens -> NavEntry(key) {
                    MinhasViagens(
                        userId = key.userId,
                        onNavigate = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                else -> {
                    error("Unknown route: $key")
                }
            }
        }
    )
}

@Composable
fun PasswordTextField(
    senha: String,
    onSenhaChange: (String) -> Unit,
    label: String = "Senha"
) {
    var passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = senha,
        onValueChange = onSenhaChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (passwordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image = if (passwordVisible.value)
                Icons.Default.Close
            else
                Icons.Default.Star

            val description = if (passwordVisible.value)
                "Ocultar senha"
            else
                "Mostrar senha"

            IconButton(
                onClick = { passwordVisible.value = !passwordVisible.value }
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = description
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GerenciamentoViagemTheme {
        MyApp()
    }
}