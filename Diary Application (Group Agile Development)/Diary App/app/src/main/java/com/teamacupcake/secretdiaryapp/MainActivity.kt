@file:OptIn(ExperimentalMaterial3Api::class)

package com.teamacupcake.secretdiaryapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamacupcake.secretdiaryapp.data.DiaryEntryViewModel
import com.teamacupcake.secretdiaryapp.data.FriendsViewModel
import com.teamacupcake.secretdiaryapp.data.FriendsViewModelFactory
import com.teamacupcake.secretdiaryapp.data.ProfileViewModel
import com.teamacupcake.secretdiaryapp.data.SignUpViewModel
import com.teamacupcake.secretdiaryapp.ui.components.DateRepository
import com.teamacupcake.secretdiaryapp.ui.screens.AccountSettingsPage
import com.teamacupcake.secretdiaryapp.ui.screens.ChangeEmailAddressPage
import com.teamacupcake.secretdiaryapp.ui.screens.NewEntry
import com.teamacupcake.secretdiaryapp.ui.screens.ForgotPasswordPage
import com.teamacupcake.secretdiaryapp.ui.screens.FriendTimelineScreen
import com.teamacupcake.secretdiaryapp.ui.screens.FriendsScreen
import com.teamacupcake.secretdiaryapp.ui.screens.HomePage
import com.teamacupcake.secretdiaryapp.ui.screens.InviteFriendsScreen
import com.teamacupcake.secretdiaryapp.ui.screens.LocationPickerScreen
import com.teamacupcake.secretdiaryapp.ui.screens.LoginPage
import com.teamacupcake.secretdiaryapp.ui.screens.MapScreen
import com.teamacupcake.secretdiaryapp.ui.screens.MoodTrackerPage
import com.teamacupcake.secretdiaryapp.ui.screens.NewEntry
import com.teamacupcake.secretdiaryapp.ui.screens.ProfileScreen
import com.teamacupcake.secretdiaryapp.ui.screens.RefreshScreen
import com.teamacupcake.secretdiaryapp.ui.screens.SearchDiaryScreenComposable
//import com.teamacupcake.secretdiaryapp.ui.screens.SearchResults
import com.teamacupcake.secretdiaryapp.ui.screens.SettingsPage
import com.teamacupcake.secretdiaryapp.ui.screens.SignupPage
import com.teamacupcake.secretdiaryapp.ui.screens.TermsAndConditionsScreen
import com.teamacupcake.secretdiaryapp.ui.theme.SecretDiaryAppTheme
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var db: FirebaseFirestore
    private var loggedIn = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        DateRepository.loadPreferences(this)
        // Log values after loading preferences
        Log.d("MainActivity", "onCreate: lastPostDate = ${DateRepository.lastPostDate}")
        Log.d("MainActivity", "onCreate: currentStreak = ${DateRepository.currentStreak}")
        Log.d("MainActivity", "onCreate: todaysStreak = ${DateRepository.todaysStreak}")

        setContent {
            SecretDiaryAppTheme {
                MainScreen()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        DateRepository.savePreferences(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        DateRepository.savePreferences(this)
    }
    enum class NavDestination {
        SIGN_UP,
        LOG_IN,
        INVITE_FRIENDS,
        HOME,
        CHAT,
        FRIENDS_TIMELINE,
        LOCATION_PICKER,
        SETTINGS,
        FORGOT_PASSWORD,
        NEW_ENTRY,
        MOOD_SUMMARY,
        ACCOUNT_SETTINGS,
        CHANGE_EMAIL_ADDRESS,
        SEARCH_DIARY,
        PROFILE,
        FRIENDS,
        MAP,
        REFRESH,
        TANDCs
    }

    sealed class NavItem(val title: String, val route: String) {
        object SignUp : NavItem("Sign Up", NavDestination.SIGN_UP.name)
        object Map : NavItem("Sign Up", NavDestination.MAP.name)
        object Refresh : NavItem("Refresh", NavDestination.REFRESH.name)
        object LogIn : NavItem("Log In", NavDestination.LOG_IN.name)
        object Friends : NavItem("Friends", NavDestination.FRIENDS.name)
        object Profile : NavItem("Profile", NavDestination.PROFILE.name)
        object FriendsTimeline : NavItem("Friend's Timeline", NavDestination.FRIENDS_TIMELINE.name)
        object LocationPicker : NavItem("Location Picker", NavDestination.LOCATION_PICKER.name)
        object InviteFriends : NavItem("Invite Friends", NavDestination.INVITE_FRIENDS.name)
        object ForgotPassword : NavItem("Forgot Password", NavDestination.FORGOT_PASSWORD.name)
        object Home : NavItem("Home", NavDestination.HOME.name)
        object Settings : NavItem("Settings", NavDestination.SETTINGS.name)
        object NewEntry : NavItem("New Entry", NavDestination.NEW_ENTRY.name)
        object MoodSummary : NavItem("Mood Summary", NavDestination.MOOD_SUMMARY.name)
        object SearchDiary : NavItem("Search Diary", NavDestination.SEARCH_DIARY.name)
        object AccountSettings : NavItem("Account Settings", NavDestination.ACCOUNT_SETTINGS.name)
        object ChangeEmailSetting : NavItem("Change Email Address", NavDestination.CHANGE_EMAIL_ADDRESS.name)
        object TandCs : NavItem("Terms and Conditions", NavDestination.TANDCs.name)
    }

    data class NavBarItem(
        val title: String,
        val destination: NavItem,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val hasNews: Boolean,
        val badgeCount: Int? = null
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavHost(
        showSnackbar: (msg: String) -> Unit,
        navController: NavController,
        startDestination: String
        ) {
        val signUpViewModel: SignUpViewModel = viewModel()
        val diaryEntryViewModel: DiaryEntryViewModel = viewModel()
        val friendsViewModelFactory = remember { FriendsViewModelFactory(diaryEntryViewModel) }
        val friendsViewModel: FriendsViewModel = viewModel(factory = friendsViewModelFactory)


//        val friendsViewModel: FriendsViewModel = viewModel()

        val profileViewModel: ProfileViewModel = viewModel()

        //val viewModelFactory: ViewModelProvider.Factory = viewModel()


        NavHost(
            navController = navController as NavHostController,
            startDestination = startDestination
        ) {
            composable(NavItem.SignUp.route) {
                SignupPage(
                    signUpViewModel = signUpViewModel,
                    onNavigateToLogin = {
                        navController.navigate(NavItem.LogIn.route)
                    }
                )
            }
            composable(NavItem.Refresh.route) {
                RefreshScreen(navController)
            }
            //val friendIds = listOf("friendId1", "friendId2", "friendId3")
            composable(NavItem.FriendsTimeline.route){
                FriendTimelineScreen(
                    friendsViewModel,
                    navController,
                    profileViewModel
                    )
            }
            composable(NavItem.LocationPicker.route) {
                LocationPickerScreen(
                    navController = navController,
                    diaryEntryViewModel = diaryEntryViewModel
                )
            }

            composable(NavItem.Map.route){
                MapScreen(
                    viewModelFactory = friendsViewModelFactory,
                    navController
                )
            }
            composable(NavItem.Friends.route){
                FriendsScreen(
                    friendsViewModel,
                    profileViewModel
                )
            }
            composable(NavItem.Profile.route){
                ProfileScreen(
                    profileViewModel
                )
            }
            composable(NavItem.LogIn.route) {
                LoginPage(auth,
                    applicationContext,
                    onNavigateToHome = {
                        navController.navigate(NavItem.Home.route) {
                            popUpTo(NavItem.LogIn.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(NavItem.ForgotPassword.route)
                    },
                    onNavigateToSignup = {
                        navController.navigate(NavItem.SignUp.route)
                    }
                )
            }
            composable(NavItem.InviteFriends.route) {
                InviteFriendsScreen(
                   friendsViewModel = FriendsViewModel(
                       diaryEntryViewModel
                   )
                )
            }
            composable(NavItem.TandCs.route) {
                TermsAndConditionsScreen()
            }
            composable(NavItem.Home.route) {
                HomePage(
                    applicationContext,
                    onNavigateToNewEntry = {
                        navController.navigate(NavItem.NewEntry.route)
                    },
                    diaryEntryViewModel,
                    navController
                )
            }
//            composable(NavDestination.CHAT.name) {
//                ChatPage()
//            }
            composable(NavDestination.SETTINGS.name) {
                SettingsPage(
                    navController,
                    onNavigateToAccount = {
                        navController.navigate(NavItem.AccountSettings.route)
                    }
                )


            }
            composable(NavDestination.FORGOT_PASSWORD.name) {
                ForgotPasswordPage(onNavigateToLogin = {
                    navController.navigate(NavItem.LogIn.route)
                })
            }
            composable(NavItem.NewEntry.route) {
                NewEntry(
                    applicationContext,
                    onNavigateToLocationPicker = {
                                                 navController.navigate((NavItem.LocationPicker.route))
                    },
                    onNavigateToHome = {
                        navController.navigate(NavItem.Home.route)
                    },
                    auth = auth,
                    navController = navController
                   //viewModel = diaryEntryViewModel
                )
            }
            composable(NavItem.MoodSummary.route) {
                MoodTrackerPage(
                    diaryEntry = diaryEntryViewModel
                )
            }
            composable(NavDestination.SEARCH_DIARY.name) {
                //SearchResults(diaryEntryViewModel, navController)
                SearchDiaryScreenComposable(diaryEntryViewModel,navController)
            }
            composable(NavItem.AccountSettings.route) {
                AccountSettingsPage(
                    onNavigateToChangeEmail = {
                        navController.navigate(NavItem.ChangeEmailSetting.route)
                    },
                    onLogout = {
                        navController.navigate(NavItem.LogIn.route) {
                            // Pop up to the login destination, clearing the back stack
                            popUpTo(NavItem.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    applicationContext,
                    auth,
                    navController // Pass NavController here

                )
            }
            composable(NavItem.ChangeEmailSetting.route) {
                ChangeEmailAddressPage(
                    onBackButtonPress = {
                        navController.popBackStack()
                    },
                    onSignOut = {
                        auth.signOut()
                        navController.navigate(NavItem.LogIn.route) {
                            popUpTo(NavItem.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    showSnackbarMessage =  { message ->
                        showSnackbar(message)
                    },
                    auth = auth
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainScreen() {
        navController = rememberNavController()
        loggedIn = auth.currentUser != null
        val startDestination by rememberSaveable {
            mutableStateOf(
                if (loggedIn) NavItem.Home.route else NavItem.LogIn.route
            )
        }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        //Define bottom navigation items
        val items = listOf(
            NavBarItem(
                title = NavItem.FriendsTimeline.title,
                destination = NavItem.FriendsTimeline,
                selectedIcon = Icons.Filled.Face,
                unselectedIcon = Icons.Outlined.Face,
                hasNews = false
            ),
            NavBarItem(
                title = NavItem.Home.title,
                destination = NavItem.Home,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false
            ),
            NavBarItem(
                title = NavItem.MoodSummary.title,
                destination = NavItem.MoodSummary,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                hasNews = false
            ),

            NavBarItem(
                title = NavItem.SearchDiary.title,
                destination = NavItem.SearchDiary,
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                hasNews = false
            ),


//            NavBarItem(
//                title = NavItem.NewEntry.title,
//                destination = NavItem.NewEntry,
//                selectedIcon = Icons.Filled.Add,
//                unselectedIcon = Icons.Outlined.Add,
//                hasNews = false
//            ),
//            NavBarItem(
//                title = NavItem.Settings.title,
//                destination = NavItem.Settings,
//                selectedIcon = Icons.Filled.Settings,
//                unselectedIcon = Icons.Outlined.Settings,
//                hasNews = false
//            )
        )

        val location = listOf(
            NavItem.LocationPicker
        )

        val navForbiddenScreens = listOf(
            NavItem.LogIn,
            NavItem.ForgotPassword,
            NavItem.SignUp,
           // NavItem.LocationPicker
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isLocationPickerScreen = navBackStackEntry?.destination?.route == NavItem.LocationPicker.route
        val isDrawerEnabled = !isLocationPickerScreen

        val currentDestination = navBackStackEntry?.destination
        val currentRoute = navBackStackEntry?.destination?.route
        //navBarVisible false when route is in navForbiddenScreens
        val navBarVisible = currentRoute !in navForbiddenScreens.map { it.route }
                && !isLocationPickerScreen
        //val navBarVisible = currentRoute !in navForbiddenScreens.map { it.route }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                if(currentRoute !in location.map { it.route }) {
                    if (navBarVisible) {
                        NavigationBar {
                            //Navigation drawer/Hamburger menu navbar item
                            if (drawerState.isClosed) {
                                NavigationBarItem(
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = "Menu",
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                )
                            } else {
                                NavigationBarItem(
                                    selected = false,
                                    onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = "Close",
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                            contentDescription = "Close menu"
                                        )
                                    }
                                )
                            }
                            //Navigation icons
                            items.forEach { item ->
                                NavigationBarItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true,
                                    onClick = {
                                        navController.navigate(item.destination.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
                                            // Avoid multiple copies of the same destination when re-selecting the same item
                                            launchSingleTop = true
                                            // Restore state when re-selecting a previously selected item
                                            restoreState = true
                                        }
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            textAlign = TextAlign.Center
                                        )
                                    },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Badge { Text(item.badgeCount.toString()) }
                                                } else if (item.hasNews) {
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (currentDestination?.hierarchy?.any { it.route == item.destination.route } == true) {
                                                    item.selectedIcon
                                                } else {
                                                    item.unselectedIcon
                                                },
                                                contentDescription = item.title
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            },
        ) {
            //Code for menu slide out drawer
            if (navBarVisible) {
                if(isDrawerEnabled) {
                    ModalNavigationDrawer(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("Options", modifier = Modifier.padding(16.dp))
                                HorizontalDivider()
                                NavigationDrawerItem(
                                    label = { Text(text = "Settings") },
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.Settings.route)
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Invite Friends") },
                                    //selected = navController.currentDestination?.route == NavItem.InviteFriends.route,
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.InviteFriends.route) {
                                            // Close the drawer when an item is clicked
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Map") },
                                    //selected = navController.currentDestination?.route == NavItem.InviteFriends.route,
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.Map.route) {
                                            // Close the drawer when an item is clicked
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Friends") },
                                    //selected = navController.currentDestination?.route == NavItem.InviteFriends.route,
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.Friends.route) {
                                            // Close the drawer when an item is clicked
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Profile") },
                                    //selected = navController.currentDestination?.route == NavItem.InviteFriends.route,
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.Profile.route) {
                                            // Close the drawer when an item is clicked
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Privacy Policy") },
                                    //selected = navController.currentDestination?.route == NavItem.InviteFriends.route,
                                    selected = false,
                                    onClick = {
                                        navController.navigate(NavItem.TandCs.route) {
                                            // Close the drawer when an item is clicked
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                )
                            }
                        },
                    ) {
                        Surface() {
                            //jetpack compose navigation controller code
                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                                showSnackbar = {snackbarMessage ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = snackbarMessage,
                                            duration = SnackbarDuration.Indefinite,
                                            actionLabel = "Ok"
                                        )
//                                        snackbarHostState.showSnackbar(snackbarMessage)
                                    }
                                }
                            )
                        }
                    }
                }

            } else {
                    Surface() {
                        //jetpack compose navigation controller code
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            showSnackbar = {snackbarMessage ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(snackbarMessage)
                                }
                            }
                        )
                    }
                }
            }
        }
    }


