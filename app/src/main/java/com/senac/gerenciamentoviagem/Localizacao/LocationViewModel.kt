package com.senac.gerenciamentoviagem.Localizacao

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LocationUiState(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val accuracy: Float? = null,
    val hasPermission: Boolean = false,
    val isLoading: Boolean = false,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)

class LocationViewModel(
    private val context: Context,
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun onPermissionGranted() {
        _uiState.update { it.copy(hasPermission = true, isLoading = true) }
        viewModelScope.launch @androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]) {

            repository.locationWithCityFlow(context)
                .catch { e -> /* tratar erro */ }
                .collect { location ->
                    _uiState.update {
                        it.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracy = location.accuracy,
                            city = location.city,
                            isLoading = false
                        )
                    }
                }

        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[APPLICATION_KEY]?.applicationContext ?: error("Context not found")
                val repository = LocationRepository(context)
                LocationViewModel(context, repository)
            }
        }
    }
}