package com.teamacupcake.secretdiaryapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.ui.graphics.vector.ImageVector
import com.teamacupcake.secretdiaryapp.MainActivity


    sealed class SettingsItem(
        val icon: ImageVector,
        val iconDesc: String,
        val title: String,
        val settingDesc: String,
        val navItem: MainActivity.NavItem
    ) {
        object Account : SettingsItem(
            Icons.Outlined.AccountCircle,
            "Account Icon",
            "Account",
            "Change/Update Account Settings",
            MainActivity.NavItem.AccountSettings
        )
    }

    sealed class AccountItem(
        val icon: ImageVector,
        val iconDesc: String,
        val title: String,
        val settingDesc: String,
        val navItem: MainActivity.NavItem
    ) {
        object ChangeEmailAddress : AccountItem(
            Icons.Outlined.Email,
            "Change email Address title",
            "Change Email Address",
            "Update/Change current email address",
            MainActivity.NavItem.ChangeEmailSetting
        )

        // New item for inviting friends
        object InviteFriends : AccountItem(
            Icons.Outlined.Add, // Assuming you have a suitable icon for inviting friends
            "Invite Friends Icon",
            "Invite Friends",
            "Generate an invite code to share with friends",
            MainActivity.NavItem.InviteFriends // You need to define this navItem in your MainActivity.NavItem sealed class
        )


    }