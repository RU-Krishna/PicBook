package com.feature.videos.ui

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.feature.videos.model.Hits
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PortraitVideoViewScreen(
    video: () -> Hits,
    query: () -> String,
    onBackPress: () -> Unit = {},
    fullScreenMode: () -> Boolean,
    onClickDownload: (String, String) -> Long,
    enterExitFullScreen: () -> Unit = {},

) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
    ) {
        item {
            PreviewVideoTopAppBar(
                onBackPress = onBackPress,
                query = query
            )
            VideoPlayer(
                modifier = Modifier.wrapContentSize().padding(horizontal = 1.dp, vertical = 1.dp),
                uri = { video().videos.medium.url },
                width = { video().videos.medium.width },
                height =  { video().videos.medium.height },
                fullScreenMode = fullScreenMode ,
                enterExitFullScreen = enterExitFullScreen,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 16.dp
                )
            )
            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = 2.dp, vertical = 2.dp
                ), thickness = 2.dp
            )
            Column {
                AuthorRow(
                    userName =  { video().user },
                    userImage =  { video().userImageURL }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 2.dp, vertical = 4.dp
                    ), thickness = 2.dp
                )
                VideoTags(
                    tags =  { video().tags }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 2.dp, vertical = 4.dp
                    ), thickness = 2.dp
                )
                ButtonSection(
                    onClickDownload = {
                        onClickDownload(
                            video().videos.medium.url,
                            "${video().id}_${(500..82596).random()}"
                        )
                    }
                )

            }

        }

    }


}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    uri: () -> String ,
    width: () -> Int ,
    height: () -> Int,
    fullScreenMode: () -> Boolean,
    enterExitFullScreen: () -> Unit,
    shape: RoundedCornerShape
) {

    val exoPlayer: Player = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(uri())
                addMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
    }

    val coroutineScope = rememberCoroutineScope()

    var totalDuration by remember {
        mutableStateOf(0L)
    }

    var controllersVisibility by remember {
        mutableStateOf(true)
    }

    var currentDuration by remember {
        mutableStateOf(exoPlayer.currentPosition.coerceAtLeast(0L))
    }

    var isPlaying by remember {
        mutableStateOf(exoPlayer.isPlaying)
    }

    //Recording Playback State...
    var playbackState by remember {
        mutableStateOf(exoPlayer.playbackState)
    }

    /*
    * Adding a clean up listener to the Player Composable in order
    * to release the resources used by the ExoPlayer.
    *
    * And also adding a listener to the Player to track the current
    * position...
    * */
    DisposableEffect(key1 = Unit) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                totalDuration = player.duration.coerceAtLeast(0L)
                currentDuration = player.currentPosition.coerceAtLeast(0L)
                isPlaying = player.isPlaying
                playbackState = player.playbackState
            }

        }
        exoPlayer.addListener(listener)  // Adding Listener to the Exo Player...
        onDispose {
            exoPlayer.removeListener(listener) // Removing listener to the exoPlayer...
            exoPlayer.release()  // Releasing resources acquired by the exoPlayer...
        }
    }


    //Controlling Controllers Visibility
    if (controllersVisibility) {
        coroutineScope.launch {
            delay(1000)
            controllersVisibility = false
        }
    }


    //Player View...
    val playerView = remember {
        PlayerView(context)
            .apply {
                this.useController = false
                this.player = exoPlayer
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.setOnClickListener {
                    controllersVisibility = true
                }
                this.setShowNextButton(false)
                this.setShowPreviousButton(false)
            }
    }

    //Card representing the PlayerView and controllers...
    ElevatedCard(
        shape = shape,
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            focusedElevation = 6.dp
        )
    ) {
        Box {
            AndroidView(
                factory = {
                    playerView
                },
                modifier = modifier.aspectRatio(
                    width().toFloat() / height().toFloat()
                )
            )
            PlayerControls(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.Center),
                controllerVisibility = { controllersVisibility },
                isPlaying =  { isPlaying },
                onTogglePlay = {
                    when {
                        exoPlayer.isPlaying ->
                            exoPlayer.pause()

                        exoPlayer.isPlaying.not() && playbackState == STATE_ENDED -> {
                            exoPlayer.seekTo(0)
                            exoPlayer.playWhenReady = true
                        }

                        else ->
                            exoPlayer.play()
                    }
                },
                replayCallback = {
                    exoPlayer.seekTo(exoPlayer.currentPosition - 5000L)
                },
                forwardCallback = {
                    exoPlayer.seekTo(exoPlayer.currentPosition + 5000L)
                },
                totalDuration =  { totalDuration } ,
                currentDuration =  { exoPlayer.currentPosition },
                onSeekChanged = {
                    exoPlayer.seekTo(it.toLong())
                },
                playbackState =  { playbackState },
                fullScreenMode = fullScreenMode,
                enterExitFullScreen = enterExitFullScreen
            )
        }


    }

}


@Composable
fun AuthorRow(
    userImage: () -> String, userName: () -> String
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = 8.dp, start = 8.dp, bottom = 4.dp
        )
    ) {
        AsyncImage(
            model = userImage(),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = userName().trim(),
            modifier = Modifier.padding(start = 16.dp),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoTags(
    tags: () -> String
) {

    val allTags = remember(tags()) {
        tags().split(",")
    }
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
            maxItemsInEachRow = 4
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

@Preview(showSystemUi = true)
@Composable
fun ButtonSection(
    modifier: Modifier = Modifier,
    onClickDownload: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .padding(
                vertical = 16.dp,
                horizontal = 12.dp
            )
            .fillMaxWidth()

    ) {
        FilledTonalButton(
            onClick = { onClickDownload() },
            elevation = ButtonDefaults.filledTonalButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 12.dp,
            ),
            modifier = modifier
                .weight(0.70f, fill = false)
                .width(240.dp)
        ) {
            Text(
                text = "Download", fontSize = 24.sp, fontStyle = FontStyle.Italic
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewVideoTopAppBar(
    onBackPress: () -> Unit = {},
    query: () -> String
) {
    TopAppBar(
        title = {
            Text(
                text = query().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    .ifEmpty { "Videos" },
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







