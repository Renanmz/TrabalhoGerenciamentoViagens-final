package com.senac.gerenciamentoviagem

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.senac.gerenciamentoviagem.Bd.AppDatabase
import com.senac.gerenciamentoviagem.EntradaTelas.EsqueciSenha
import com.senac.gerenciamentoviagem.EntradaTelas.NovoLogin
import com.senac.gerenciamentoviagem.EntradaTelas.TelaMain
import com.senac.gerenciamentoviagem.Localizacao.LocationRepository
import com.senac.gerenciamentoviagem.Localizacao.ViagemRepository
import com.senac.gerenciamentoviagem.MainTelas.Fotos.FotosTela
import com.senac.gerenciamentoviagem.MainTelas.MinhasViagens
import com.senac.gerenciamentoviagem.MainTelas.NovaViagem
import com.senac.gerenciamentoviagem.MainTelas.Principal.Principal
import com.senac.gerenciamentoviagem.MainTelas.Principal.PrincipalViewModelFactory
import com.senac.gerenciamentoviagem.MainTelas.Principal.Roteiro.GeminiService
import com.senac.gerenciamentoviagem.MainTelas.Principal.Roteiro.RoteiroRepository
import com.senac.gerenciamentoviagem.MainTelas.Principal.Roteiro.RoteiroTela
import com.senac.gerenciamentoviagem.MainTelas.Principal.Roteiro.RoteiroViewModel
import com.senac.gerenciamentoviagem.Rotas.RouteEditarViagem
import com.senac.gerenciamentoviagem.Rotas.RouteEsqueciSenha
import com.senac.gerenciamentoviagem.Rotas.RouteFotos
import com.senac.gerenciamentoviagem.Rotas.RouteMain
import com.senac.gerenciamentoviagem.Rotas.RouteMinhasViagens
import com.senac.gerenciamentoviagem.Rotas.RouteNovaViagem
import com.senac.gerenciamentoviagem.Rotas.RouteNovoLogin
import com.senac.gerenciamentoviagem.Rotas.RoutePrincipal
import com.senac.gerenciamentoviagem.Rotas.RouteRoteiro
import com.senac.gerenciamentoviagem.ViewModel.PrincipalViewModel
import com.senac.gerenciamentoviagem.ui.theme.GerenciamentoViagemTheme
import okhttp3.OkHttpClient
import org.osmdroid.config.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(
                "osmdroid",
                MODE_PRIVATE
            )
        )
        enableEdgeToEdge()
        setContent {
            GerenciamentoViagemTheme {
                    MyApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyApp() {
    val backStack = rememberNavBackStack(RouteMain) //primeira rota

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {

                is RouteRoteiro -> NavEntry(key) {
                    val context = LocalContext.current
                    val db = AppDatabase.getDatabase(context)

                    // Setup Retrofit with extra long timeouts for complex AI routes
                    val retrofit = remember {
                        val okHttpClient = OkHttpClient.Builder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .callTimeout(120, TimeUnit.SECONDS) // Tempo total da chamada
                            .build()

                        Retrofit.Builder()
                            .baseUrl("https://generativelanguage.googleapis.com/")
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                    }
                    val service = remember { retrofit.create(GeminiService::class.java) }
                    val repository = remember { RoteiroRepository(service) }
                    val viewModel: RoteiroViewModel = viewModel(factory = object :
                        ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return RoteiroViewModel(repository, db.viagemDao()) as T
                        }
                    })

                    // Load travel data automatically
                    LaunchedEffect(key.viagemId) {
                        viewModel.carregarViagem(key.viagemId)
                    }

                    RoteiroTela(
                        viagemDestino = viewModel.viagemAtual?.destino ?: "",
                        viewModel = viewModel,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

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

                    val context = LocalContext.current

                    val db = AppDatabase.getDatabase(context)

                    val principalViewModel: PrincipalViewModel = viewModel(
                        factory = PrincipalViewModelFactory(
                            locationRepository = LocationRepository(context),

                            viagemRepository = ViagemRepository(
                                db.viagemDao()
                            )
                        )
                    )
                    Principal(
                        email = key.email,

                        onNavigate = {backStack.removeLastOrNull()},
                        onNovaViagem = { backStack.add(RouteNovaViagem(key.userId))},
                        onMinhasViagens = {backStack.add(RouteMinhasViagens(key.userId))},
                        onFotosClick = { viagemId ->backStack.add(
                            RouteFotos(
                                userId = key.userId,
                                viagemId = viagemId
                            )
                        )},
                        onRoteiroClick = { viagemId ->backStack.add(
                            RouteRoteiro(
                                userId = key.userId,
                                viagemId = viagemId
                            )
                        )},
                        viewModel = principalViewModel
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
                        },
                        onEditar = { viagem ->
                            backStack.add(
                                RouteEditarViagem(
                                    key.userId,
                                    viagem
                                )
                            )
                        }
                    )
                }
                is RouteEditarViagem -> NavEntry(key) {
                    NovaViagem(
                        userId = key.userId,
                        viagemEditar = key.viagem,
                        onNavigate = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is RouteFotos -> NavEntry(key) {
                    FotosTela(
                        viagemId = key.viagemId,
                        userId = key.userId,
                        onBack = {
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GerenciamentoViagemTheme {
        MyApp()
    }
}
