package com.feature.videos.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player.STATE_ENDED
import com.feature.videos.R
import java.util.concurrent.TimeUnit

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    controllerVisibility: () -> Boolean,
    isPlaying: () -> Boolean,
    onTogglePlay: () -> Unit,
    playbackState: () -> Int,
    replayCallback: () -> Unit,
    forwardCallback: () -> Unit,
    totalDuration: () -> Long,
    currentDuration: () -> Long,
    onSeekChanged: (Float) -> Unit,
    fullScreenMode: () -> Boolean,
    enterExitFullScreen: () -> Unit



) {
    AnimatedVisibility(
        visible = controllerVisibility(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(modifier = modifier) {
            CenterControls(
                modifier = Modifier.align(Alignment.Center),
                isPlaying =  isPlaying,
                onTogglePlay = onTogglePlay,
                replayCallback = replayCallback,
                forwardCallback = forwardCallback,
                playbackState = playbackState
            )
            BottomControls(
                modifier = Modifier.align(Alignment.BottomCenter),
                totalDuration = totalDuration,
                currentDuration = currentDuration,
                onSeekChanged = onSeekChanged,
                fullScreenMode = fullScreenMode,
                enterExitFullScreen = enterExitFullScreen
            )

        }
    }
}


@Composable
fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    onTogglePlay: () -> Unit,
    replayCallback: () -> Unit,
    forwardCallback: () -> Unit,
    playbackState: () -> Int

) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = replayCallback, modifier = Modifier.weight(0.40f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.replay_5),
                contentDescription = "Replay by 5 seconds",
                tint = Color.White,
                modifier = modifier
                    .size(40.dp)
                    .alpha(0.8f)


            )
        }
        IconButton(
            onClick = onTogglePlay,
            modifier = Modifier.weight(0.20f)
        ) {
            Icon(
                painter = painterResource(
                    id = when {
                        isPlaying() -> R.drawable.pause_circle
                        isPlaying().not() && playbackState() == STATE_ENDED -> R.drawable.replay
                        else -> R.drawable.play_circle
                    }
                ),
                contentDescription = "Play or Pause",
                tint = Color.White,
                modifier = modifier
                    .size(40.dp)
                    .alpha(0.8f)


            )
        }
        IconButton(
            onClick = forwardCallback,
            modifier = Modifier.weight(0.40f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.forward_5),
                contentDescription = "Forward by 5 seconds",
                tint = Color.White,
                modifier = modifier
                    .size(40.dp)
                    .alpha(0.8f)

            )
        }
    }
}

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentDuration: () -> Long,
    onSeekChanged: (Float) -> Unit,
    fullScreenMode: () -> Boolean,
    enterExitFullScreen: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Bottom, modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${totalDuration().formatMinSec()}/${currentDuration().formatMinSec()}",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(start = 8.dp)
                    .align(Alignment.Bottom)
                    .alpha(0.8f)

            )
            IconButton(
                onClick = {
                           enterExitFullScreen()
                          },
                modifier = Modifier
                    .weight(0.2f)
                    .align(Alignment.Bottom),

                ) {
                Icon(
                    painter = painterResource(
                        id = if(fullScreenMode())
                        R.drawable.fullscreen_exit
                        else
                        R.drawable.fullscreen
                    ),
                    contentDescription = "Enter/Exit Full Screen",
                    tint = Color.White,
                    modifier = modifier
                        .size(20.dp)
                        .alpha(0.8f)
                )
            }

        }
        Box {
            Slider(
                value = currentDuration().toFloat(),
                onValueChange = {
                    onSeekChanged(it)
                },
                valueRange = 0f..totalDuration().toFloat(),
                modifier = Modifier.align(Alignment.BottomCenter)
            )

        }


    }

}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}