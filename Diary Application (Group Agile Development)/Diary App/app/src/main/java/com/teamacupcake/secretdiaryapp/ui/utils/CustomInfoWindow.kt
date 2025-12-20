package com.teamacupcake.secretdiaryapp.ui.utils

import android.util.Log
import android.view.View
import android.widget.TextView
import com.teamacupcake.secretdiaryapp.R
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class CustomInfoWindow(mapView: MapView) : InfoWindow(R.layout.custom_marker_window, mapView) {
    override fun onOpen(item: Any?) {
        Log.d("MapController", "CustomInfoWindow opened for item: $item")
        val title = view.findViewById<TextView>(R.id.info_window_title)
        val content = view.findViewById<TextView>(R.id.info_window_content)

        // Assuming item is your DiaryEntry object, or adjust accordingly
        if (item is DiaryEntry) {
            title.text = item.title
            content.text = item.content

        }
    }

    override fun onClose() {
        // Optional: Handle the info window close event
    }
}
