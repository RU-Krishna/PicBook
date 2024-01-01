package com.common.error.emptyResult

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.common.error.R

@Preview(showSystemUi = true)
@Composable
fun EmptyImageResultScreen(
    text: String = ""
) {
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.void_image),
            contentDescription = "No Images",
            modifier = Modifier
                .aspectRatio(
                    16f / 9f
                )
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "No Images Found for \'${text}\'",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
    
}

@Preview(showSystemUi = true)
@Composable
fun EmptyVideoResultScreen(
    text: String = ""
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.void_image),
            contentDescription = "No Images",
            modifier = Modifier
                .aspectRatio(
                    1f
                )
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "No Videos Found for \'${text}\'",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }

}


