package com.senac.gerenciamentoviagem.ViewModel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagem.Bd.ViagemDao
import com.senac.gerenciamentoviagem.ViewModel.MinhasViagensViewModel

class MinhasViagensViewModelFactory(
    private val dao: ViagemDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MinhasViagensViewModel(dao) as T
    }
}