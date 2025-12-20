package com.teamacupcake.secretdiaryapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.teamacupcake.secretdiaryapp.R
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.FriendsViewModel
import com.teamacupcake.secretdiaryapp.data.ProfileViewModel
import com.teamacupcake.secretdiaryapp.data.UserProfile
import com.teamacupcake.secretdiaryapp.data.fetchFriendProfileCoroutine
import com.teamacupcake.secretdiaryapp.ui.components.DiaryEntryItem
import com.teamacupcake.secretdiaryapp.ui.utils.CustomInfoWindow
import com.teamacupcake.secretdiaryapp.ui.utils.getTimeAgo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.BoundingBox
import java.net.URL


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FullScreenMapComposable(
    modifier: Modifier = Modifier,
    navController: NavController,
    diaryEntryViewModel: DiaryEntryViewModel,

    onLocationSelected: (Double, Double, String) -> Unit
) {
    val context = LocalContext.current

    // Configuration setup
    Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

    var showDialog by remember { mutableStateOf(false) }
    val defaultCenter = GeoPoint(54.574227, -1.234956)
    var selectedLocation by remember { mutableStateOf(defaultCenter) }
    var locationName by remember { mutableStateOf("") }

        // Define the logic to fetch the current location
    fun fetchCurrentLocation(onLocationFetched: (GeoPoint) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLocation = GeoPoint(location.latitude, location.longitude)
                Log.d("FullScreenMapComposable", "Current location: $currentLocation")

                onLocationFetched(currentLocation)
            } else {
                Log.d("FullScreenMapComposable", "Location is null, cannot fetch current location")

            }
        }
    }

    // Define permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("FullScreenMapComposable", "Location permission granted: $isGranted")

        if (isGranted) {
            fetchCurrentLocation { currentLocation ->
                selectedLocation = currentLocation
            } } else {
            Log.d("FullScreenMapComposable", "Location permission not granted")

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val entries by diaryEntryViewModel.entries.observeAsState(initial = emptyList())
        entries?.sortedByDescending { it.timestamp }
        val mostRecentEntry = entries?.firstOrNull() // Take the most recent entry, if any

        AndroidView(
            modifier = Modifier.matchParentSize(), // Ensures the map fills the Box
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                    controller.setZoom(15.0)
                    controller.setCenter(defaultCenter)

                    // Initialize the marker at the center
                    val marker = Marker(this).apply {
                        position = defaultCenter
                        icon = ContextCompat.getDrawable(context, R.drawable.baseline_location_on_24)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        isDraggable = true // Allow dragging of the marker
                        setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                            override fun onMarkerDrag(marker: Marker?) {}
                            override fun onMarkerDragEnd(marker: Marker?) {
                                // Update location when drag ends
                                marker?.position?.let {
                                    selectedLocation = it
                                    Log.v("Marker", "Marker dragged to: $it")
                                }
                            }
                            override fun onMarkerDragStart(marker: Marker?) {}
                        })
                    }
                    overlays.add(marker)
                }
            },
            update = { mapView ->
                val marker = mapView.overlays.firstOrNull { it is Marker } as Marker?
                if (marker != null) {
                    if (marker.position != selectedLocation) {
                        Log.d("FullScreenMapComposable", "Updating marker and map center to new location: $selectedLocation")
                        mapView.controller.setCenter(selectedLocation)
                        marker.position = selectedLocation
                        mapView.invalidate() // Redraw the MapView to show updated marker position
                    }
                }
            }
        )
    }

    // Show dialog to get location name from user
    LocationNameDialog(
        locationName = locationName,
        onValueChange = { newValue ->
            Log.v("FullScreenMapComposable", "Location name changed: $newValue")
            locationName = newValue
        },
        onSave = {
            Log.v("FullScreenMapComposable", "Saving location: $selectedLocation with name: $locationName")

            if (locationName != "") {
                onLocationSelected(
                    selectedLocation.latitude,
                    selectedLocation.longitude,
                    locationName
                )
                navController.popBackStack()
            }else {
                Toast.makeText( context,"Please name your location", Toast.LENGTH_SHORT).show()

            }
        },
        onUseCurrentLocation = {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        },
        onNavigateToEntry = {
            Log.v("FullScreenMapComposable", "Navigating back")
            navController.popBackStack()
        })
    diaryEntryViewModel.setLocationDetails(selectedLocation.latitude,
        selectedLocation.longitude,
        locationName)

}







@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationNameDialog(
    locationName: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onUseCurrentLocation: () -> Unit,
    onNavigateToEntry: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Name this Location"
            , style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = locationName,
            singleLine = true,
            onValueChange = onValueChange,
            label = { Text("Location Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick =
        onUseCurrentLocation,
            modifier = Modifier.fillMaxWidth()) {
            Text("Use Current Location")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) { Text("Save") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onNavigateToEntry,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) { Text("Return to Entry") }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapScreen(viewModelFactory: ViewModelProvider.Factory, navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid.orEmpty()
    val coroutineScope = rememberCoroutineScope()
    val friendsViewModel: FriendsViewModel = viewModel(factory = viewModelFactory)
    var mapInitialized by remember { mutableStateOf(false) }


    // State for managing dialog visibility and selected diary entry
    var showDiaryEntryDialog by remember { mutableStateOf(false) }
    var selectedDiaryEntry by remember { mutableStateOf<DiaryEntry?>(null) }
    var defaultCenter by remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
    var boundingBox by remember { mutableStateOf(BoundingBox(0.0, 0.0,0.0,0.0)) }



    // Call fetchFriends to load friends when the screen is composed
    LaunchedEffect(Unit) {
        Log.d("MapScreen", "Fetching friends list")
        friendsViewModel.fetchFriends()
    }
    val friendsList by friendsViewModel.friendsList.observeAsState(initial = emptyList())

// Assuming you have a LiveData in your ViewModel that combines both user's and friends' entries

    val profileViewModel: ProfileViewModel = viewModel() // Use viewModel() to get the ViewModel instance provided by Hilt or ViewModelProvider

    val combinedEntries by friendsViewModel.combinedEntries.observeAsState(initial = emptyList())
    // Constants to determine padding
    val LATITUDE_PADDING = 0.5 // degrees of padding for latitude
    val LONGITUDE_PADDING = 0.5 // degrees of padding for longitude

    LaunchedEffect(combinedEntries) {
        val validEntries = combinedEntries.filter { it.latitude != null && it.longitude != null }
        if (validEntries.isNotEmpty()) {
            val avLat = validEntries.mapNotNull { it.latitude }.average()
            val avLon = validEntries.mapNotNull { it.longitude }.average()
            // Determine the bounding box
            val minLat = validEntries.minOf { it.latitude!! }
            val maxLat = validEntries.maxOf { it.latitude!! }
            val minLon = validEntries.minOf { it.longitude!! }
            val maxLon = validEntries.maxOf { it.longitude!! }

             boundingBox = BoundingBox(maxLat + LATITUDE_PADDING, maxLon + LONGITUDE_PADDING, minLat - LATITUDE_PADDING, minLon - LONGITUDE_PADDING)

            defaultCenter = GeoPoint(avLat, avLon)
        } else {
             defaultCenter = GeoPoint(54.574227, -1.234956)
        }
    }




    // Initialize the MapController with a callback
    val mapController = remember {
        MapController(context, friendsViewModel, profileViewModel, coroutineScope).also { controller ->
            controller.onMarkerClicked = { diaryEntry ->
                selectedDiaryEntry = diaryEntry
                showDiaryEntryDialog = true
            }
        }
    }
    LaunchedEffect(key1 = friendsList) {
        Log.d("MapScreen", "Friends list changed, fetching friend entries")
        val friendIds = friendsList.map { it.userId2 }
        friendsViewModel.getFriendEntries(currentUserId, friendIds)
    }

    LaunchedEffect(combinedEntries) {
        Log.d("MapScreen", "Combined entries changed, updating markers: $combinedEntries")
        coroutineScope.launch {
            mapController.updateMarkers(combinedEntries, profileViewModel, friendsViewModel)
        }
    }

    MapViewContainer(
        //defaultCenter: GeoPoint,
        mapController = mapController,
        defaultCenter = defaultCenter,
        boundingBox =  boundingBox,
        mapInitialized = mapInitialized,
        setMapInitialized = { mapInitialized = it },
        onMarkerClicked = { diaryEntry ->
            selectedDiaryEntry = diaryEntry
            showDiaryEntryDialog = true
        }
    )

    // Display the dialog if showDiaryEntryDialog is true and a diary entry is selected
    if (showDiaryEntryDialog && selectedDiaryEntry != null) {
        DiaryEntryDialog(
            entry = selectedDiaryEntry!!,
            onDismiss = {
                showDiaryEntryDialog = false
                selectedDiaryEntry = null
            },
            friendsViewModel = friendsViewModel,
            navController = navController,
                    profileViewModel = profileViewModel
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiaryEntryDialog(
    entry: DiaryEntry,
    onDismiss: () -> Unit,
    friendsViewModel: FriendsViewModel,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val diaryEntryViewModel: DiaryEntryViewModel = viewModel()
    var friendUsername by remember { mutableStateOf("") }
    var currentUsername by remember { mutableStateOf("") }

    var friendProfilePicUrl by remember { mutableStateOf("") }
    var entryImageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    // Instead of observing LiveData here, call a suspend function or another non-LiveData function
    // that can fetch the UserProfile without affecting the rest of the composable items
    var userProfile by remember { mutableStateOf(UserProfile()) }

    LaunchedEffect(entry.userId) {
        // Consider using a coroutine to fetch the user profile and update the state directly
        userProfile = entry.userId?.let { profileViewModel.fetchUserProfileSuspend(it) } ?: UserProfile(
            "Loading...",
            ""
        )
        // Now check if storageReference is valid before fetching the image from the storage reference
        entry.storageReference?.takeIf { it.isNotBlank() }?.let { storageReference ->
            val storageRef = FirebaseStorage.getInstance().reference.child(storageReference)
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                entry.imageUrl = uri.toString()
                isLoading = false // Update loading status after all data is fetched
            }.addOnFailureListener {
                Log.e("DiaryEntryDialog", "Failed to fetch entry image.", it)
                isLoading = false // Update loading status even if there's an error
            }
        } ?: run {
            // Handle the case where storageReference is null or empty
            isLoading = false // No image to load, update loading status
            Log.e("DiaryEntryDialog", "Storage reference is null or empty.")
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Dialog(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.padding(16.dp)) {


                entry.locationName?.let { locName ->
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                        Text(
                            text = locName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.width(8.dp))

                    // Assuming you have a Coil dependency for loading images
                    if (userProfile.profilePictureUrl.isNotEmpty()) {
                        Image(
                            painter = rememberImagePainter(userProfile.profilePictureUrl),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape) // Assuming you want a circular image
                        )
                    } else {
                        // Display a placeholder if there's no URL
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Username and time ago
                    Text(text = userProfile.username, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = getTimeAgo(entry.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                // Entry content
                DiaryEntryItem(
                    entry = entry,
                    onEntryDeleted = {}, // Handle entry deletion
                    onPasswordEntered = {}, // Handle password entry
                    onEntryClick = {}, // Handle entry click
                    onNavigateToHome = {}, // Handle navigation to home
                    isFriendEntry = true, // Assume it's a friend's entry for now
                    navController = navController
                )
            }
        }
    }
}



class MapController(
        private val context: Context,
        private val viewModel: FriendsViewModel,
        private val profileViewModel: ProfileViewModel,
        private val coroutineScope: CoroutineScope
    ) {
        private var mapView: MapView? = null

        var onMarkerClicked: ((DiaryEntry) -> Unit)? = null



    fun initView(mapView: MapView, defaultCenter: GeoPoint) {
            this.mapView = mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                //controller.setCenter(GeoPoint(54.5620327, -1.2422312))

                controller.setCenter(GeoPoint(defaultCenter))
                zoomToBoundingBox(boundingBox, true)
                Log.d("set center", "default center: $defaultCenter" )




            }
        }

    suspend fun updateMarkers(
            entries: List<DiaryEntry>,
            profileViewModel: ProfileViewModel,
            friendsViewModel: FriendsViewModel
        ) = coroutineScope {
            Log.d("MapController", "updateMarkers() called with ${entries.size} entries")
            mapView?.overlays?.clear()
            Log.d("MapController", "Clearing previous markers")

            entries.forEach { entry ->
                entry.latitude?.let { latitude ->
                    entry.longitude?.let { longitude ->
                            entry.userId?.let { userId ->
                                launch {
                                    val isUserEntry = userId == profileViewModel.currentUserId
                                    val profile = if (isUserEntry) {
                                        try {
                                            profileViewModel.fetchUserProfileSuspend(userId)
                                        } catch (e: Exception) {
                                            Log.e(
                                                "updateMarkers",
                                                "Error fetching user profile: $e"
                                            )
                                            null
                                        }
                                    } else {
                                        try {
                                            friendsViewModel.fetchFriendProfileCoroutine(userId)
                                        } catch (e: Exception) {
                                            Log.e(
                                                "updateMarkers",
                                                "Error fetching friend profile: $e"
                                            )
                                            null
                                        }
                                    }


                                    profile?.let {
                                        val drawable = if (isUserEntry) ContextCompat.getDrawable(
                                            context,
                                            R.drawable.user_marker
                                        )
                                        else ContextCompat.getDrawable(
                                            context,
                                            R.drawable.baseline_location_on_24
                                        )
                                        val marker = createMarker(entry, profile.username, drawable)
                                        if(!isUserEntry && entry.private){}
                                        else {
                                            mapView?.overlays?.add(marker)
                                            Log.d(
                                                "MapController",
                                                "Marker added for entry: ${entry.title}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            withContext(Dispatchers.Main) {
                mapView?.invalidate()
                Log.d("MapController", "MapView invalidated to reflect marker changes.")
            }
        }


        private suspend fun createMarker(
            entry: DiaryEntry,
            username: String,
            drawable: Drawable?
        ): Marker {
            Log.d("MapController", "Attempting to create a marker for entry: ${entry.title}")


            val mapViewNonNull =
                mapView ?: throw IllegalStateException("MapView not initialized").also {
                    Log.e(
                        "MapController",
                        "MapView is not initialized when creating marker for entry: ${entry.title}"
                    )
                }

            return Marker(mapViewNonNull).apply {
                position = GeoPoint(entry.latitude!!, entry.longitude!!)
                icon = drawable
                title = username

                Log.d("MapController", "Setting info window for marker of entry: ${entry.title}")
                // setInfoWindow(CustomInfoWindow(mapViewNonNull))

                snippet = "${entry.title}:${entry.content}"
                Log.d("MapController", "Marker created with position: $position and title: $title")

                infoWindow = CustomInfoWindow(mapViewNonNull) // Attach your CustomInfoWindow here

                // Set a click listener for the marker if needed
                // Modify the click listener to use the callback
                setOnMarkerClickListener { clickedMarker, _ ->
                    onMarkerClicked?.invoke(entry) // Invoke the callback with the entry

                    true // We've handled the click event
                }
            }
        }

        private suspend fun loadImageFromUrl(url: String): Drawable? {
            return withContext(Dispatchers.IO) {

                try {
                    val inputStream = URL(url).openStream()
                    Log.e("MapController", "Success loading image from URL ")

                    Drawable.createFromStream(inputStream, url)

                } catch (e: Exception) {
                    Log.e("MapController", "Error loading image from URL: $e")
                    null
                }
            }
        }
    }

    @Composable
    fun MapViewContainer(    defaultCenter: GeoPoint,
                             boundingBox: BoundingBox,
                             mapInitialized: Boolean,
                             setMapInitialized: (Boolean) -> Unit,
                             mapController: MapController, onMarkerClicked: (DiaryEntry) -> Unit) {


       // val ANIMATION_DELAY = 1000 // 1 second
        // Use LaunchedEffect to introduce a one-off effect that sets the map to initialized after 5 seconds
        LaunchedEffect(key1 = "initMap") {
            delay(5000) // delay for 5 seconds
            setMapInitialized(true) // after delay, set map as initialized to block further updates
        }

        // MapView composable with an AndroidView to host your mapView
        AndroidView(
            factory = { context ->
                MapView(context).also {
                    mapController.initView(it, defaultCenter)

                    // Setup markers and their click listeners
                    // Call onMarkerClicked(diaryEntry) when a marker is clicked
                }
            },
            update = { mapView ->
                Log.d("map intitialized?", "$mapInitialized")
                if (!mapInitialized) {
                    mapView.controller.setCenter(defaultCenter)
                    mapView.zoomToBoundingBox(boundingBox, true)
                    //setMapInitialized(true) // Use the passed method to update state


                    // Update mapView with new markers or changes.
                    // You can access `mapController` to update markers
                    // and set click listeners to call `onMarkerClicked`
                }
            }
        )
    }
