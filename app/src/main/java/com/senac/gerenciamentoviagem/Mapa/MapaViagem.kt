package com.senac.gerenciamentoviagem.Mapa

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapaViagem(
    latitude: Double,
    longitude: Double,
    titulo: String
) {
    val destino = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destino, 12f)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = destino),
            title = titulo
        )
    }
}