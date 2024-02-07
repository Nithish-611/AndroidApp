package com.example.famone.ui.theme.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.famone.utils.DocumentUtil

@Composable
fun CategoryDropDown(expanded: MutableState<Boolean>, selectedOption: MutableState<String>) {

    Column {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            val list = DocumentUtil.getCategories()
            for (item in list){
                DropdownMenuItem(
                    text = {Text(item)},
                    onClick = {
                        selectedOption.value = item
                        expanded.value = false
                    }
                )
            }
        }
    }
}