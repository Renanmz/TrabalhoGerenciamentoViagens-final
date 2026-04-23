package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

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


    suspend fun validarLogin(): Boolean = withContext(Dispatchers.IO) {
        val users = userDao.findAll()
        val userValido = users.find {
            it.email.trim().equals(_email.value.trim(), ignoreCase = true) &&
                    it.senha.trim() == _senha.value.trim()
        }
        return@withContext userValido != null
    }
}