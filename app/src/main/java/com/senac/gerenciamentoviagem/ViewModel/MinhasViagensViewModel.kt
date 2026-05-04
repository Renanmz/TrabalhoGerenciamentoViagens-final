package com.senac.gerenciamentoviagem.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MinhasViagensViewModel(
    private val viagemDao: ViagemDao
) : ViewModel() {

    private val _viagens = MutableStateFlow<List<Viagem>>(emptyList())
    val viagens = _viagens.asStateFlow()

    fun carregar(userId: Int) {
        viewModelScope.launch {
            _viagens.value = viagemDao.findByUser(userId)
        }
    }

    fun excluir(viagem: Viagem, userId: Int) {
        viewModelScope.launch {
            viagemDao.delete(viagem)
            carregar(userId)
        }
    }
}