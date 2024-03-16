package com.example.famone.ui.theme.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.famone.R

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.delete_confirmation_title)) },
        text = { Text(text = stringResource(id = R.string.delete_confirmation_message)) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
                , colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = stringResource(id = R.string.delete), color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
                , colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
