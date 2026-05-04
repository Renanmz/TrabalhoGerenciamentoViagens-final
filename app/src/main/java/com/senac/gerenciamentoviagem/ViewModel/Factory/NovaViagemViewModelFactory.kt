package com.senac.gerenciamentoviagem.ViewModel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.ViewModel.NovaViagemViewModel

class NovaViagemViewModelFactory(
    private val viagemDao: ViagemDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NovaViagemViewModel(viagemDao) as T
    }
}