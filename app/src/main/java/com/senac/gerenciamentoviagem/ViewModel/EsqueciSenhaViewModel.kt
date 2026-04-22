package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

class EsqueciSenhaViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // validação simples de email
    val isEmailValid: StateFlow<Boolean> =
        _email.map { email ->
            email.isNotBlank() && email.contains("@")
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onEmailChange(valor: String) {
        _email.value = valor
    }
}