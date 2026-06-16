package com.senac.gerenciamentoviagem.ViewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagem.Localizacao.LocationRepository
import com.senac.gerenciamentoviagem.Localizacao.ViagemRepository
import com.senac.gerenciamentoviagem.MainTelas.Principal.PrincipalUiState
import com.senac.gerenciamentoviagem.Model.Viagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class PrincipalViewModel(
    private val locationRepository: LocationRepository,
    private val viagemRepository: ViagemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrincipalUiState())
    val uiState: StateFlow<PrincipalUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @androidx.annotation.RequiresPermission(
        allOf = [
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    fun buscarViagemAtual(context: android.content.Context) {

        viewModelScope.launch {

            locationRepository.locationWithCityFlow(context)
                .collect { location ->

                    val cidade = location.city ?: return@collect
                    val dataAtual = LocalDate.now().toString()

                    val viagem = viagemRepository.buscarViagemAtual(
                        cidade,
                        dataAtual
                    )

                    _uiState.update {
                        it.copy(
                            cidadeAtual = cidade,
                            viagemAtual = viagem,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            loading = false
                        )
                    }
                }
        }
    }
}



fun abrirMapa(context: Context, cidade: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(cidade)}")

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    context.startActivity(intent)
}