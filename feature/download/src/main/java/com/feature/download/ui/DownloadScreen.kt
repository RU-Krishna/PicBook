package com.feature.download.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.common.error.emptyResult.EmptyDownloadImageScreen
import com.common.error.emptyResult.EmptyDownloadVideoScreen
import com.common.media.imageMedia.Images
import com.common.media.videoMedia.Videos
import com.feature.download.R

@Preview(showSystemUi = true)
@Composable
fun DownloadsScreen(
    imageList: () -> List<Images> = { listOf() },
    videoList: () -> List<Videos> = { listOf() },
    onImageCardClick: () -> Unit = {},
    onVideoCardClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        var selected by remember {
            mutableStateOf(true)
        }

        DownloadTopSection(
            selected = selected,
            onImageSelected = {
                selected = true
            },
            onVideoSelected = {
                selected = false
            }
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier
                .padding(
                    horizontal = 2.dp,
                    vertical = 2.dp
                )
        )
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AnimatedVisibility(
                visible = imageList().isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyDownloadImageScreen()

            }
            AnimatedVisibility(
                visible = imageList().isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                LazyColumn {
                    item {
                    }
                    items(items = imageList(), key = {
                        it.id
                    }) {
                        ImageCard(uri = it.uri.toString())
                    }
                }
            }

        }
        AnimatedVisibility(
            visible = !selected,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AnimatedVisibility(
                visible = videoList().isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyDownloadVideoScreen()
            }
            AnimatedVisibility(
                visible = videoList().isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                LazyColumn {
                    items(items = videoList(), key = {
                        it.id
                    }) {
                        VideoCard(
                            uri = it.uri.toString()
                        )
                    }
                }
            }

        }


    }


}

@Preview(showSystemUi = true)
@Composable
fun ImageCard(
    context: Context = LocalContext.current,
    uri: String = ""

) {
    val model = remember {
        ImageRequest.Builder(context)
            .data(uri)
            .crossfade(true)
            .crossfade(2000)
            .build()
    }
    ElevatedCard(
        onClick = { imageViewerIntent(
            context,
            uri
        ) },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(
            topEnd = 8.dp,
            topStart = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 8.dp
        )
    ) {
        Box() {
            AsyncImage(
                model = model,
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(
                    16f / 9f
                )
            )

        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun VideoCard(
    context: Context = LocalContext.current,
    uri: String = ""
) {

    val model = remember {
        ImageRequest.Builder(context)
            .data(uri)
            .videoFrameMillis(1000L)
            .decoderFactory { result, options, _ ->
                VideoFrameDecoder(
                    result.source,
                    options
                )
            }
            .crossfade(true)
            .crossfade(2000)
            .build()
    }

    ElevatedCard(
        onClick = { videoViewerIntent(
            context,
            uri
        ) },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(
            topEnd = 8.dp,
            topStart = 16.dp,
            bottomEnd = 16.dp,
            bottomStart = 8.dp
        )
    ) {
        Box {
            AsyncImage(
                model = model,
                contentDescription = "Video",
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(
                    16f / 9f
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.play_circle),
                contentDescription = "Play Video",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                tint = Color.White
            )
        }

    }
}


@Composable
fun DownloadTopSection(
    selected: Boolean = false,
    onImageSelected: () -> Unit = {},
    onVideoSelected: () -> Unit = {}
) {


    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .safeDrawingPadding()

    ) {
        ElevatedFilterChip(
            selected = selected,
            onClick = { onImageSelected() },
            label = {
                Text(
                    text = "Images",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
            },
            elevation = FilterChipDefaults.elevatedFilterChipElevation(6.dp),
            leadingIcon = {
                AnimatedVisibility(
                    visible = selected,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Image Selected"
                    )

                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        ElevatedFilterChip(
            selected = !selected,
            onClick = { onVideoSelected() },
            label = {
                Text(
                    text = "Videos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
            },
            elevation = FilterChipDefaults.elevatedFilterChipElevation(6.dp),
            leadingIcon = {
                AnimatedVisibility(
                    visible = !selected,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Video Selected"
                    )

                }
            }
        )


    }

}

private fun imageViewerIntent(
    context: Context,
    uri: String,
) {
    val intent = Intent()
    intent.setAction(Intent.ACTION_VIEW)
        .setDataAndType(Uri.parse(uri), "image/*")
    context.startActivity(intent)

}

private fun videoViewerIntent(
    context: Context,
    uri: String,
) {
    val intent = Intent()
    intent.setAction(Intent.ACTION_VIEW)
        .setDataAndType(Uri.parse(uri), "video/*")
    context.startActivity(intent)

}






