package com.senac.gerenciamentoviagem.Rotas

import androidx.navigation3.runtime.NavKey
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.serialization.Serializable

@Serializable
data object RouteMain: NavKey
@Serializable
data object RouteNovoLogin: NavKey
@Serializable
data object RouteEsqueciSenha: NavKey
@Serializable
data class RoutePrincipal(val userId: Int, val email: String) : NavKey
@Serializable
data class RouteNovaViagem(val userId: Int) : NavKey
@Serializable
data class RouteMinhasViagens(val userId: Int) : NavKey
@Serializable
data class RouteEditarViagem(val userId: Int, val viagem: Viagem) : NavKey