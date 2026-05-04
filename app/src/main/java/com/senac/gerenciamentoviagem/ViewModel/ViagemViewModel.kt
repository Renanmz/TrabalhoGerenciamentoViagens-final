package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViagemViewModel(
    private val viagemDao: ViagemDao
) : ViewModel() {

    private val _viagens = MutableStateFlow<List<Viagem>>(emptyList())
    val viagens: StateFlow<List<Viagem>> = _viagens

    fun carregar(userId: Int) {
        viewModelScope.launch {
            _viagens.value = viagemDao.findByUser(userId)
        }
    }

    fun inserir(viagem: Viagem, onDone: () -> Unit) {
        viewModelScope.launch {
            viagemDao.insert(viagem)
            onDone()
        }
    }

    fun atualizar(viagem: Viagem, onDone: () -> Unit) {
        viewModelScope.launch {
            viagemDao.update(viagem)
            onDone()
        }
    }

    fun excluir(viagem: Viagem, userId: Int) {
        viewModelScope.launch {
            viagemDao.delete(viagem)
            carregar(userId)
        }
    }
}