package com.senac.gerenciamentoviagem.MainTelas.Principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senac.gerenciamentoviagem.Localizacao.LocationRepository
import com.senac.gerenciamentoviagem.Localizacao.ViagemRepository
import com.senac.gerenciamentoviagem.ViewModel.PrincipalViewModel

class PrincipalViewModelFactory(
    private val locationRepository: LocationRepository,
    private val viagemRepository: ViagemRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PrincipalViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return PrincipalViewModel(
                locationRepository,
                viagemRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}