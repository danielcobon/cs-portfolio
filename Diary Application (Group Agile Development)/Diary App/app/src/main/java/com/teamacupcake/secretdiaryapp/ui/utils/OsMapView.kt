package com.teamacupcake.secretdiaryapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
//import com.google.android.gms.maps.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import androidx.compose.ui.Modifier
import org.osmdroid.views.MapView


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    onMapReady: (MapView) -> Unit = {}
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                onMapReady(this)
            }
        },
        update = { mapView ->
            // Update actions if necessary
        }
    )
}
