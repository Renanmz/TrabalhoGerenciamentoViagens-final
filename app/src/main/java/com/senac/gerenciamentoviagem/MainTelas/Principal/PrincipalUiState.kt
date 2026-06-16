package com.senac.gerenciamentoviagem.MainTelas.Principal

import com.senac.gerenciamentoviagem.Model.Viagem

data class PrincipalUiState(
    val cidadeAtual: String? = null,
    val viagemAtual: Viagem? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val loading: Boolean = false
)