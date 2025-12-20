//package com.teamacupcake.secretdiaryapp.ui.utils
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import com.teamacupcake.secretdiaryapp.MoodTrackerPage
//import com.teamacupcake.secretdiaryapp.ui.screens.AccountSettingsScreen
//import com.teamacupcake.secretdiaryapp.ui.screens.SearchDiaryScreen
//
//class NavigationUtils {
//    enum class NavDestination {
//        SIGN_UP,
//        LOG_IN,
//        HOME,
//        CHAT,
//        SETTINGS,
//        FORGOT_PASSWORD,
//        NEW_ENTRY,
//        MOOD_SUMMARY,
//        ACCOUNT_SETTINGS,
//        CHANGE_EMAIL_ADDRESS,
//        SEARCH_DIARY
//    }
//
//    sealed class NavItem(val title: String, val route: String) {
//        object SignUp : NavItem("Sign Up", NavDestination.SIGN_UP.name)
//        object LogIn : NavItem("Log In", NavDestination.LOG_IN.name)
//        object ForgotPassword : NavItem("Forgot Password", NavDestination.FORGOT_PASSWORD.name)
//        object Home : NavItem("Home", NavDestination.HOME.name)
//        object Settings : NavItem("Settings", NavDestination.SETTINGS.name)
//        object NewEntry : NavItem("New Entry", NavDestination.NEW_ENTRY.name)
//        object MoodSummary : NavItem("Mood Summary", NavDestination.MOOD_SUMMARY.name)
//        object SearchDiary : NavItem("Search Diary", NavDestination.SEARCH_DIARY.name) // Add this
//
//        object AccountSettings : NavItem("Account Settings", NavDestination.ACCOUNT_SETTINGS.name)
//        object ChangeEmailSetting : NavItem("Change Email Address", NavDestination.CHANGE_EMAIL_ADDRESS.name)
//    }
//
//    data class NavBarItem(
//        val title: String,
//        val destination: NavItem,
//        val selectedIcon: ImageVector,
//        val unselectedIcon: ImageVector,
//        val hasNews: Boolean,
//        val badgeCount: Int? = null
//    )
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @Composable
//    fun NavHost(navController: NavController, startDestination: String) {
//        androidx.navigation.compose.NavHost(
//            navController = navController as NavHostController,
//            startDestination = startDestination
//        ) {
//            composable(NavItem.SignUp.route) {
//                SignupPage(auth,
//                    applicationContext,
//                    onNavigateToLogin = {
//                        navController.navigate(NavItem.LogIn.route)
//                    }
//                )
//            }
//            composable(NavItem.LogIn.route) {
//                LoginPage(auth,
//                    applicationContext,
//                    onNavigateToHome = {
//                        navController.navigate(NavItem.Home.route)
//                    },
//                    onNavigateToForgotPassword = {
//                        navController.navigate(NavItem.ForgotPassword.route)
//                    },
//                    onNavigateToSignup = {
//                        navController.navigate(NavItem.SignUp.route)
//                    }
//                )
//            }
//            composable(NavItem.Home.route) {
//                HomePage(
//                    applicationContext,
//                    onNavigateToNewEntry = {
//                        navController.navigate(NavItem.NewEntry.route)
//                    }
//                )
//            }
//            composable(NavDestination.CHAT.name) {
//                ChatPage()
//            }
//            composable(NavDestination.SETTINGS.name) {
//                SettingsPage(
//                    onNavigateToAccount = {
//                        navController.navigate(NavItem.AccountSettings.route)
//                    }
//                )
//            }
////                    composable(forgotPasswordNav) {
////                        ForgotPasswordPage(Modifier, navController)
//            composable(NavDestination.FORGOT_PASSWORD.name) {
//                ForgotPasswordPage(onNavigateToLogin = {
//                    navController.navigate(NavItem.LogIn.route)
//                })
//            }
//            composable(NavItem.NewEntry.route) {
//                NewEntry(
//                    applicationContext,
//                    onNavigateToHome = {
//                        navController.navigate(NavItem.Home.route)
//                    },
//                    auth
//                )
//            }
//            composable(NavItem.MoodSummary.route) {
//                MoodTrackerPage(
//                    year = "2024",
//                    month = "03",
//                    data = mapOf(
//                        Pair("Happy", 15),
//                        Pair("Sad", 3),
//                        Pair("Angry", 2),
//                        Pair("Love", 6),
//                        Pair("Shocked", 5)
//                    )
//                )
//            }
//            composable(NavDestination.SEARCH_DIARY.name) {
//                SearchDiaryScreenComposable()
//            }
//            composable(NavItem.AccountSettings.route) {
//                AccountSettingsScreen.AccountSettingsPage(
//                    onNavigateToChangeEmail = {
//                        navController.navigate(NavItem.ChangeEmailSetting.route)
//                    },
//                    onLogout = {
//                        navController.navigate(NavItem.LogIn.route) {
//                            // Pop up to the login destination, clearing the back stack
//                            popUpTo(NavItem.LogIn.route) {
//                                inclusive = true
//                            }
//                        }
//                    }
//                )
//            }
//            composable(NavItem.ChangeEmailSetting.route) {
//                ChangeEmailAddressPage()
//            }
//        }
//    }
//}