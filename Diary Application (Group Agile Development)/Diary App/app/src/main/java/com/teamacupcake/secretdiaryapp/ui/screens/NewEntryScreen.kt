package com.teamacupcake.secretdiaryapp.ui.screens


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.data.DiaryEntry
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import com.teamacupcake.secretdiaryapp.ui.components.DateRepository
import com.teamacupcake.secretdiaryapp.ui.components.EmojiDropdownMenu
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.absoluteValue


@SuppressLint("NotConstructor", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NewEntry(
        context: Context,
        onNavigateToHome: () -> Unit,
        navController: NavController,
        onNavigateToLocationPicker: () -> Unit,
        auth: FirebaseAuth
        // diaryEntryViewModel: DiaryEntryViewModel
    ) {
    // Obtain an instance of DiaryEntryViewModel scoped to the current composable
    val diaryEntryViewModel: DiaryEntryViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    val moods = listOf("😀", "😞", "😠", "😍", "🤯") // List of emojis
    var selectedIndex = remember { mutableStateOf(0) } // Index of the selected emoji
    val maxCharLimit = 500
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val charCount = text.text.length
    var showMessage by remember { mutableStateOf(false) }
    var charsLeft by remember { mutableStateOf(maxCharLimit) }
    var showInstructions by remember { mutableStateOf(false) } // State for displaying instructions
    var isSecret by remember { mutableStateOf(false) } // Track if the entry is secret
    var password by remember { mutableStateOf("") } // Password for the secret entry
    var isSecretDialogVisible by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var isPublic by remember { mutableStateOf(false) } // Add this for the public/private toggle
    val secretBackgroundColor = Color(0xFF9370DB) // Lighter purple color for secret entries
    val storageReference by remember { mutableStateOf(null) }

    //val entries by diaryEntryViewModel.entries.observeAsState(initial = emptyList())




    val backgroundColor = if (isSecret) {
        secretBackgroundColor // Use lighter purple for secret entries
    } else {
        Color.White
    }// Default background color for non-secret entries

    // Before navigating to LocationPickerScreen, save current form data
    fun saveFormData() {
        // Example: Saving a title (assuming it's one of your form fields)
        // Save each form field to `savedStateHandle`
        navController.currentBackStackEntry?.savedStateHandle?.set("entryTitle", title)
        navController.currentBackStackEntry?.savedStateHandle?.set(
            "selectedIndex",
            selectedIndex.value
        )
        navController.currentBackStackEntry?.savedStateHandle?.set("text", text.text)
        navController.currentBackStackEntry?.savedStateHandle?.set("charsLeft", charsLeft)
        navController.currentBackStackEntry?.savedStateHandle?.set("isSecret", isSecret)
        navController.currentBackStackEntry?.savedStateHandle?.set("password", password)
        navController.currentBackStackEntry?.savedStateHandle?.set("isPublic", isPublic)
        // Now navigate
        onNavigateToLocationPicker()
    }
    // Upon returning to this screen, restore the state
    val savedTitle =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>("entryTitle")
    if (savedTitle != null) {
        title = savedTitle
        // Clear the saved state
        navController.previousBackStackEntry?.savedStateHandle?.remove<String>("entryTitle")
        navController.previousBackStackEntry?.savedStateHandle?.remove<Int>("selectedIndex")
        navController.previousBackStackEntry?.savedStateHandle?.remove<String>("text")
        navController.previousBackStackEntry?.savedStateHandle?.remove<Int>("charsLeft")
        navController.previousBackStackEntry?.savedStateHandle?.remove<Boolean>("isSecret")
        navController.previousBackStackEntry?.savedStateHandle?.remove<String>("password")
        navController.previousBackStackEntry?.savedStateHandle?.remove<Boolean>("isPublic")
    }

    LaunchedEffect(navController) {
        val backStackEntry = navController.currentBackStackEntry?.savedStateHandle

        // Restore the title
        backStackEntry?.get<String>("entryTitle")?.let {
            title = it
        }

        // Restore selectedIndex. Assume you've stored it as Int. If not, you'll need to adjust the storing logic as well.
        backStackEntry?.get<Int>("selectedIndex")?.let {
            selectedIndex.value =
                it // Since selectedIndex is a MutableState<Int>, assign the restored value to its value property
        }

        // Restore text. Convert the stored String back into TextFieldValue.
        backStackEntry?.get<String>("text")?.let {
            text = TextFieldValue(it)
        }

        // Restore charsLeft
        backStackEntry?.get<Int>("charsLeft")?.let {
            charsLeft = it
        }

        // Restore isSecret
        backStackEntry?.get<Boolean>("isSecret")?.let {
            isSecret = it
        }

        // Restore password
        backStackEntry?.get<String>("password")?.let {
            password = it
        }

        // Restore isPublic
        backStackEntry?.get<Boolean>("isPublic")?.let {
            isPublic = it
        }
    }


    // Initialize the variables to hold the location data. Consider providing default values.
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var locationName by remember { mutableStateOf<String?>(null) }

    // Observe the result from savedStateHandle
    val locationResult = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("locationKey")?.observeAsState()

    locationResult?.value?.let { locationData ->
        // Split the string back into its components or parse the object
        val (lat, lon, name) = locationData.split(",")
        // Use the data to update the UI or ViewModel
        Log.d("NewEntry", "Received location result: Lat=$lat, Lon=$lon, Name=$name")
        // Assign the values to the variables
        latitude = lat.toDoubleOrNull()
        longitude = lon.toDoubleOrNull()
        locationName = name

        // Optionally, clear the result to not handle it again
        navController.currentBackStackEntry?.savedStateHandle?.set("locationKey", null)
    }

    val onLocationSelected: (Double, Double, String) -> Unit = { lat, lon, name ->
        //diaryEntryViewModel.setLocationDetails(lat, lon, name)
        // Optionally navigate back or handle other UI logic here if needed
        Log.v("LocationPickerScreen", "Location selected: Lat=$lat, Lon=$lon, Name=$name")
        // Assuming you might want to pop back to the previous screen after selecting a location:
    }


    // If you need to observe changes, you can do so like this
    val observedLocationName by remember { mutableStateOf(diaryEntryViewModel.locationName) }
    val observedLatitude by remember { mutableStateOf(diaryEntryViewModel.latitude) }
    val observedLongitude by remember { mutableStateOf(diaryEntryViewModel.longitude) }


//    var title by remember { mutableStateOf("") }
//        val moods = listOf("😀", "😞", "😠", "😍", "🤯") // List of emojis
//        var selectedIndex = remember { mutableStateOf(0) } // Index of the selected emoji
//        val maxCharLimit = 500
//        var text by remember { mutableStateOf(TextFieldValue("")) }
//        val charCount = text.text.length
//        var showMessage by remember { mutableStateOf(false) }
//        var charsLeft by remember { mutableStateOf(maxCharLimit) }
//        var showInstructions by remember { mutableStateOf(false) } // State for displaying instructions
//        var isSecret by remember { mutableStateOf(false) } // Track if the entry is secret
//        var password by remember { mutableStateOf("") } // Password for the secret entry
//        var isSecretDialogVisible by remember { mutableStateOf(false) }
//        var showPassword by remember { mutableStateOf(false) }
//        var isPublic by remember { mutableStateOf(false) } // Add this for the public/private toggle

    var showDialog by remember { mutableStateOf(false) }
    //var onLocationSelected: (Double, Double, String) -> Unit = { lat, lon, name ->
    // Handle location selection
    showDialog = false
    // }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(backgroundColor), // Apply background color here
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // Define your New Entry content here
        // Add a button or gesture to call onBack when you want to go back to HomePage
        Button(
            onClick = {
                onNavigateToHome()
            },
            modifier = Modifier
                .padding(8.dp) // Reduced padding around the button
                .height(36.dp) // Specify the height of the button
                .fillMaxWidth() // Specify the minimum width of the button or use .width() for an exact width
        ) {
            Text("Back to Home", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(4.dp)) // Adjust the height as needed to push the toolbar down

        // Toolbar with buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 20.dp), // Adjust horizontal padding
            horizontalArrangement = Arrangement.SpaceBetween // Space out the buttons evenly
        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(48.dp) // Set the size of the Box to accommodate the IconButton
//                    .border(
//                        1.dp,
//                        Color.White,
//                        shape = CircleShape
//                    ) // Add a white border with CircleShape
//                    .background(Color.White, shape = CircleShape) // Add a white background
//            ) {
            IconButton(
                onClick = {
                    // Clear the text content of the entry
                    text = TextFieldValue("")
                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete all text")
                }
            }
            IconButton(
                onClick = {
                    showDialog = true;
                    saveFormData()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Select Location"
                )
            }


            // Button 4
            IconButton(
                onClick = {
                    isSecretDialogVisible = true // Show the password dialog
                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Lock, contentDescription = "Lock Entry")
                }
            }


            // Display password dialog when isSecretDialogVisible is true
            if (isSecretDialogVisible) {
                AlertDialog(
                    onDismissRequest = {
                        isSecretDialogVisible = false
                    }, // Dismiss the dialog if clicked outside
                    title = { Text("Set Password") },
                    text = {
                        // Add a text field for entering the password
                        // You can add other password-related fields like confirm password, etc.
                        Column {
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            showPassword = !showPassword
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Toggle Password Visibility"
                                        )
                                    }
                                }
                            )
                            if (password.isEmpty()) {
                                Text(text = "Please enter a password", color = Color.Red)
                            }
                        }
                    },

                    confirmButton = {
                        Button(
                            onClick = {
                                if (password.isNotEmpty()) {
                                    // Save the password and dismiss the dialog
                                    isSecret = true // Mark the entry as secret
                                    // You'll need to handle saving the password to the entry
                                    // For example, you can save it to the ViewModel or directly to the entry object
                                    // In this example, I'll save it directly to the entry object
                                    // Password saving logic here...
                                    // Then dismiss the dialog
                                    isSecretDialogVisible = false
                                }
                            }
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                // Dismiss the dialog without saving the password
                                isSecretDialogVisible = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }


            // Button 5
            IconButton(
                onClick = {
                    showInstructions = !showInstructions // Toggle instructions visibility
                }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Info, contentDescription = "Bino?")
                    // Show arrow icon only when instructions are visible
                    if (showInstructions) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Minimize Instructions"
                        )
                    }
                }
            }
        }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Make entry public")
        Switch(
            checked = isPublic,
            onCheckedChange = { isPublic = it }
        )
    }

    // Display instructions when the information button is pressed
    if (showInstructions) {
        Text(
            text = "To create a new entry, simply enter a title, write your diary " +
                    "content, select a mood, and optionally add an image. Then, click the save button to save your entry. " +
                    "The buttons on the tool bar allow for a quick delete if you're worried someone is looking, and also " +
                    "an option to add a password for your most secret diary entries! \uD83D\uDE00 ",
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Start
        )
    }


//        // Switch for making the entry public or not
//        Row(
//            verticalAlignment = Alignment.CenterVertically, // Align elements vertically at the center
//            horizontalArrangement = Arrangement.Start, // Start elements from the left
//            modifier = Modifier.fillMaxWidth()
//        )
//        {
//            Text(
//                "Make entry public",
//                modifier = Modifier.padding(end = 8.dp) // Add padding to separate text and switch
//            )
//            Switch(
//                checked = isPublic,
//                onCheckedChange = { isPublic = it },
//                modifier = Modifier.padding(start = 8.dp) // Add padding to separate text and switch
//            )
//        }


    // var showMessage by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(4.dp)) // Adjust the height as needed to push the toolbar down

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 56.dp) // Set a minimum height for the Row
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { input ->
                val sanitizedInput = input.replace("\n", "")
                if (sanitizedInput.length <= 30) {
                    title = sanitizedInput
                }
            },
            label = { Text("Title") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), // Maintain end padding
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        // Wrapping EmojiDropdownMenu in a Box for better control over its position
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically) // This ensures the Box itself is centered vertically in the Row
        ) {
            // Apply offset here if needed or adjust padding within EmojiDropdownMenu
            EmojiDropdownMenu(
                itemList = moods,
                selectedIndex = selectedIndex,
                modifier = Modifier.offset(y = 3.dp), // Nudge the dropdown down by 10.dp
                onItemClick = { mood ->
                    // Now you can handle the selected mood here
                    // For example, you might want to show a message, store the selected mood, etc.
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(4.dp)) // Adjust the height as needed to push the toolbar down

    // Entry text field styled like OutlinedTextField and taking up 2/3 of the screen
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .heightIn(
                min = 0.dp,
                max = LocalConfiguration.current.screenHeightDp.dp * (2f / 3f) / LocalDensity.current.density
            )
            .background(Color.White, RoundedCornerShape(4.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                // Only update if the newText's length is within maxCharLimit
                if (newText.text.length <= maxCharLimit) {
                    text = newText
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
                    Text("Your Secret Diary Entry", style = TextStyle(color = Color.Gray))
                }
                innerTextField()
            }
        )
    }

        // Character counter
        Text(
            text = "Characters left: $charsLeft",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 32.dp),
            style = TextStyle(color = Color.Black, fontSize = 14.sp)
        )


        // Update the character count whenever the text changes
        LaunchedEffect(text) {
            charsLeft = maxCharLimit - text.text.length
        }

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add image button
                Button(
                    onClick = {
                        scope.launch {
                            pickImageLauncher.launch("image/*")
                        }
                    },
                    modifier = Modifier.padding(end = 16.dp, top = 16.dp)
                ) {
                    Text("Add Image", fontSize = 24.sp)
                }

                // Conditionally render the button based on isSecret state
                //if (isSecret) {
                val currentTime = System.currentTimeMillis()
                val newEntryRef = FirebaseFirestore.getInstance().collection("diaryEntries")
                    .document() // This creates a new document reference with a unique ID
                val currentDate = DateRepository.selectedDate
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDate.format(formatter) // Format date
                Log.d("date used", "Date used: ${DateRepository.selectedDate}")
                    // Add a button for handling secret entries
                    Button(onClick = {
                        if (imageUri != null) {
                            diaryEntryViewModel.uploadImage(
                                imageUri = imageUri,
                                onSuccess = { imageUrl, storagePath ->
                                    createAndInsertDiaryEntry(
                                        diaryEntryViewModel = diaryEntryViewModel,
                                        title = title,
                                        text = text,
                                        moodIndex = selectedIndex.value,
                                        imageUrl = imageUrl,
                                        userId = FirebaseAuth.getInstance().currentUser?.uid,
                                        date =  formattedDate,

                                            password = password,
                                        isPublic = isPublic,
                                        latitude = latitude,
                                        longitude = longitude,
                                        locationName = locationName,
                                        storagePath = storagePath,
                                        onNavigateToHome = onNavigateToHome,
                                        showMessage = showMessage,
                                        moods = moods,
                                        isSecret = isSecret
                                    )
                                },
                                onError = { exception ->
                                    // Handle the upload error
                                }
                            )

                        } else {
                            // Handle case where there is no image to upload
                            createAndInsertDiaryEntry(
                                diaryEntryViewModel = diaryEntryViewModel,
                                title = title,
                                text = text,
                                moodIndex = selectedIndex.value,
                                imageUrl = "",
                                userId = FirebaseAuth.getInstance().currentUser?.uid,
                                date =  formattedDate,
                                password = password,
                                isPublic = isPublic,
                                latitude = latitude,
                                longitude = longitude,
                                locationName = locationName,
                                storagePath = "",
                                onNavigateToHome = onNavigateToHome,
                                showMessage = showMessage,
                                moods = moods,
                                isSecret = isSecret
                            )
                        }
                        //showMessage.value = true

                    })



                        //modifier = Modifier.padding(end = 16.dp, top = 16.dp)
                     {
                        Text(text = "Save", fontSize = 24.sp)
                    }
                }
            }  // Display the location details at the bottom
            locationName?.let { locName ->
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

            // Display the image and the remove image button below the add image button and save button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 0.dp,
                        max = LocalConfiguration.current.screenHeightDp.dp * (3f / 5f) / LocalDensity.current.density // Adjusted to take up more room
                    )
            ) {
                // Box for the image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp) // Increased padding to accommodate the remove image button
                ) {
                    imageUri?.let { uri ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Picked image",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Box for the remove image button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // Adjust height as needed
                        .align(Alignment.BottomCenter) // Position at the bottom center
                ) {
                    // Render the remove image button only when an image is present
                    if (imageUri != null) {
                        Button(
                            onClick = { imageUri = null },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Remove Image")
                        }
                    }
                }
            }
            // If imageUri is null, display an empty Spacer to maintain the layout
            if (imageUri == null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // Adjust height as needed
                )
            }
        }

        if (showMessage) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("The entry has been saved.")
            }
        }
    }





@Composable
    fun EmojiDropdownMenu(
        itemList: List<String>,
        selectedIndex: MutableState<Int>,
        modifier: Modifier = Modifier,
        onItemClick: (Int) -> Unit
    ) {
        var showDropdown by remember { mutableStateOf(false) }

        val shape = RoundedCornerShape(8.dp)
        val border = BorderStroke(1.dp, Color.Gray)

        Box(
            modifier = modifier
                .background(Color.White, shape = shape)
                .clickable { showDropdown = true }
                .padding(12.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape
                ), // Adjust border color and width as needed
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemList[selectedIndex.value],
                modifier = Modifier.padding(8.dp)
            )
        }


        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(Color.White, shape)
                .padding(start = 8.dp, top = 4.dp)
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedIndex.value = index
                        onItemClick(index)
                        showDropdown = false
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
@RequiresApi(Build.VERSION_CODES.O)
fun createAndInsertDiaryEntry(
    diaryEntryViewModel: DiaryEntryViewModel,
    title: String,
    text: TextFieldValue,
    moodIndex: Int,
    imageUrl: String,
    userId: String?,
    date: String,
    password: String,
    isPublic: Boolean,
    latitude: Double?,
    longitude: Double?,
    locationName: String?,
    storagePath: String,
    onNavigateToHome: () -> Unit,
    showMessage: Boolean,
    moods: List<String>,
    isSecret: Boolean
) {
    val currentTime = System.currentTimeMillis()

    val diaryEntry = DiaryEntry(
        docId = UUID.randomUUID().toString(), // Assuming a UUID for new entry
        title = title,
        content = text.text,
        timestamp = currentTime,
        mood = moods[moodIndex],
        imageUrl = imageUrl,
        userId = userId,
        date = date,
        passLocked = isSecret,
        password = password,
        private = !isPublic,
        latitude = latitude,
        longitude = longitude,
        locationName = locationName,
        storageReference = storagePath
    )

    diaryEntryViewModel.insert(diaryEntry)
    // Parse the date from the string
    val entryDate = LocalDate.parse(date)

// Compare and update the last post date if necessary
    if (entryDate.isAfter(LocalDate.parse(DateRepository.lastPostDate.toString()))) {
        DateRepository.lastPostDate = entryDate

    }

    // Check if the entry date is equal to today's date and todaysStreak is false
    if (!DateRepository.todaysStreak && entryDate.isEqual(LocalDate.now())) {
        DateRepository.currentStreak += 1
        DateRepository.todaysStreak = true  // Update todaysStreak to prevent further increments today
    }

    onNavigateToHome()
//    showMessage.value = true
}



