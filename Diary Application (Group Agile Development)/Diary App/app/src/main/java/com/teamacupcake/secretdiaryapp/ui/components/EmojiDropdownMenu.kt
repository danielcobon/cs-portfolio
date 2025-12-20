package com.teamacupcake.secretdiaryapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EmojiDropdownMenu {

    @SuppressLint("NotConstructor")
    @Composable
    fun EmojiDropdownMenu(
        itemList: List<String>,
        selectedIndex: MutableState<Int>,
        modifier: Modifier = Modifier,
        onItemClick: (Int) -> Unit
    ) {
        var showDropdown by remember { mutableStateOf(false) }

        val shape = RoundedCornerShape(8.dp)
        val border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)

        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant, shape)
                .clickable { showDropdown = true }
                .padding(16.dp)
                .border(border, shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemList[selectedIndex.value],
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, shape)
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


}