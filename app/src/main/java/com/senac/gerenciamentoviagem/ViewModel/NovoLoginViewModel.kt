package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Model.User
import com.senac.gerenciamentoviagem.Bd.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NovoLoginViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _nome = MutableStateFlow("")
    val nome = _nome.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _telefone = MutableStateFlow("")
    val telefone = _telefone.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha = _senha.asStateFlow()

    private val _confirmarSenha = MutableStateFlow("")
    val confirmarSenha = _confirmarSenha.asStateFlow()

    val senhaMatch = combine(_senha, _confirmarSenha) { s, c ->
        s == c
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val isFormValid = combine(
        _nome, _email, _telefone, _senha, _confirmarSenha
    ) { n, e, t, s, c ->
        n.isNotBlank() &&
                e.isNotBlank() &&
                t.isNotBlank() &&
                s.isNotBlank() &&
                c.isNotBlank() &&
                s == c
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun onNomeChange(v: String) { _nome.value = v }
    fun onEmailChange(v: String) { _email.value = v }
    fun onTelefoneChange(v: String) { _telefone.value = v }
    fun onSenhaChange(v: String) { _senha.value = v }
    fun onConfirmarSenhaChange(v: String) { _confirmarSenha.value = v }

    fun cadastrar(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userDao.insert(
                User(
                    nome = _nome.value,
                    email = _email.value,
                    telefone = _telefone.value,
                    senha = _senha.value
                )
            )

            onSuccess()
        }
    }
}