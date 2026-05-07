package com.senac.gerenciamentoviagem.ViewModel.Login.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagem.Bd.UserDao
import com.senac.gerenciamentoviagem.ViewModel.Login.NovoLoginViewModel

class NovoLoginViewModelFactory(
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NovoLoginViewModel(userDao) as T
    }
}