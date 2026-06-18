package com.senac.gerenciamentoviagem.Localizacao

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@SuppressLint("ClickableViewAccessibility")
@Composable
fun MapaDestino(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.clipToBounds(),
        factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)
                setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            v.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    false
                }
            }
        },
        update = { mapView ->
            val ponto = GeoPoint(
                latitude,
                longitude
            )
            val centroAtual = mapView.mapCenter
            if (centroAtual.latitude != latitude || centroAtual.longitude != longitude) {
                mapView.controller.setCenter(ponto)
            }
            mapView.controller.setZoom(14.0)
            mapView.overlays.clear()
            val marcador = Marker(mapView)
            marcador.position = ponto
            marcador.title = "Destino"
            mapView.overlays.add(marcador)
            mapView.invalidate()
        }
    )
}