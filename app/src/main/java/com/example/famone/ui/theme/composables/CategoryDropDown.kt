package com.example.famone.ui.theme.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun CategoryDropDown(expanded: MutableState<Boolean>, selectedOption: MutableState<String>) {

    Column {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            DropdownMenuItem(
                text = {Text("Electronics")},
                onClick = {
                    selectedOption.value = "Electronics"
                    expanded.value = false
                }
            )
            DropdownMenuItem(
                text = {Text("Household")},
                onClick = {
                    selectedOption.value = "Household"
                    expanded.value = false
                }
            )
            DropdownMenuItem(
                text = {Text("General")},
                onClick = {
                    selectedOption.value = "General"
                    expanded.value = false
                }
            )
        }
    }
}