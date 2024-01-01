package com.feature.images.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.feature.images.R
import com.feature.images.model.Hits

@Composable
fun PreviewImage(
    onBackPress: () -> Unit = {}, hits: Hits = Hits(),
    onClickDownload: (String, String) -> Long
) {

// Define mutable state variables to keep track of the scale and offset.
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }


    var loadingState by remember {
        mutableStateOf(true)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        PreviewImageTopAppBar(
            onBackPress = onBackPress
        )
        LazyColumn(
            verticalArrangement = Arrangement.Top, modifier = Modifier
                .fillMaxSize()
                .padding(
                    8.dp
                )
        ) {
            item {

                ElevatedCard(
                    shape = RoundedCornerShape(
                        topStart = 32.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 32.dp
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)


                ) {

                    Log.d("hits", hits.toString())

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(model = hits.fullHDURL,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 32.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 16.dp,
                                        bottomEnd = 32.dp
                                    )
                                )
                                .aspectRatio(
                                    hits.imageWidth.toFloat() / hits.imageHeight.toFloat()
                                )
                                .pointerInput(Unit) {
                                    detectTransformGestures(panZoomLock = true) { _, pan, zoom, _ ->

                                        scale *= zoom

                                        scale = scale.coerceIn(1f, 100f)

                                        offset = if (scale == 1f) Offset(0f, 0f) else offset + pan

                                    }
                                }
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offset.x,
                                    translationY = offset.y
                                ),
                            contentScale = ContentScale.Fit,
                            onSuccess = {
                                loadingState = false
                            }


                        )
                        if (loadingState) CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.align(Alignment.Center)
                        )

                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 2.dp, vertical = 4.dp
                    ), thickness = 2.dp
                )
                AuthorRow(
                    userImage = hits.userImageURL, userName = hits.user
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 2.dp, vertical = 4.dp
                    ), thickness = 2.dp
                )
                ImageTags(
                    tags = hits.tags
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 2.dp, vertical = 4.dp
                    ), thickness = 2.dp
                )
                DownloadButton(
                    modifier = Modifier.padding(vertical = 16.dp),
                    onClickDownload = { onClickDownload(
                        hits.fullHDURL,
                        "${hits.id}_${(100..537593).random()}")
                    }

                )


            }
        }
    }

}

@Composable
fun AuthorRow(
    userImage: String = "", userName: String = ""
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = 8.dp, start = 8.dp, bottom = 4.dp
        )
    ) {
        AsyncImage(
            model = userImage,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(id = R.drawable.person_24px)

        )
        Text(
            text = userName.trim(),
            modifier = Modifier.padding(start = 16.dp),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageTags(
    tags: String = ""
) {

    val allTags = tags.split(",")
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = 8.dp, start = 8.dp, bottom = 4.dp
        )
    ) {
        Text(
            text = "Tags: ",
            modifier = Modifier.padding(start = 2.dp),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        FlowRow(
            maxItemsInEachRow = 3
        ) {
            repeat(allTags.size) {
                AssistChip(onClick = { /*TODO*/ }, label = {
                    Text(text = allTags[it].trim())
                }, shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(4.dp)
                )
            }
        }

    }

}

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    onClickDownload: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        FilledTonalButton(
            onClick = { onClickDownload() },
            elevation = ButtonDefaults.filledTonalButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 12.dp,
            ), modifier = modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Download", fontSize = 24.sp, fontStyle = FontStyle.Italic
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewImageTopAppBar(
    onBackPress: () -> Unit= {}
) {
    TopAppBar(
        title = {
            Text(
                text = "Krishna",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Move Back",
                modifier = Modifier
                    .clickable {
                        onBackPress()
                    }
                    .padding(start = 4.dp),
                tint = MaterialTheme.colorScheme.inversePrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
    )
}