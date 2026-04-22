package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha

    val isLoginEnabled: StateFlow<Boolean> =
        combine(_email, _senha) { email, senha ->
            email.isNotBlank() && senha.isNotBlank()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onEmailChange(novo: String) {
        _email.value = novo
    }

    fun onSenhaChange(novo: String) {
        _senha.value = novo
    }
}