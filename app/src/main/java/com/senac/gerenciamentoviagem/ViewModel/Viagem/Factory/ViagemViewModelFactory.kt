package com.senac.gerenciamentoviagem.ViewModel.Viagem.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.ViewModel.Viagem.ViagemViewModel

class ViagemViewModelFactory(
    private val viagemDao: ViagemDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViagemViewModel(viagemDao) as T
    }
}