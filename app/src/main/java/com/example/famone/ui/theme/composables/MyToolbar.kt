package com.example.famone.ui.theme.composables
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyToolbar(
    title: String,
    onCloseClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = Modifier.height(56.dp),
        navigationIcon = {
            IconButton(onClick = onCloseClicked) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
