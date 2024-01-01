package com.example.picBook.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.picBook.R

@Preview(showSystemUi = true)
@Composable
fun NetworkChangeDialog(
    onClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AlertDialog(
            onDismissRequest = { onClick() },
            confirmButton = {
//                            TextButton(onClick = { /*TODO*/ }) {
//                                Text(text = "Ok")
//
//                            }
            },
            dismissButton = {
                            TextButton(onClick = { onClick() }) {
                                Text(text = "Dismiss")
                            }

            },
            icon = {
                   Icon(
                       painter = painterResource(R.drawable.no_internet),
                       contentDescription = "No Internet")

            },
            title = {
                    Text(text = "No Internet")
            },
            text = {
                Text(text = "Looks Like, You are Offline!" +
                        "\nPlease check your Internet Connection or," +
                        "\nTry Again Later.")
            },
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 4.dp,
                bottomStart = 4.dp,
                bottomEnd = 24.dp
            ),
            tonalElevation = 8.dp,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )

    }

}