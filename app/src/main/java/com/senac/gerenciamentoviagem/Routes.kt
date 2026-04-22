package com.senac.gerenciamentoviagem

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object RouteMain: NavKey
@Serializable
data object RouteNovoLogin: NavKey
@Serializable
data object RouteEsqueciSenha: NavKey
@Serializable
data class RoutePrincipal(val email: String): NavKey