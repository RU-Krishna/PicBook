package com.feature.videos.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.common.error.emptyResult.EmptyVideoResultScreen
import com.feature.videos.R
import com.feature.videos.model.Hits
import com.feature.videos.model.Videos

@Composable
fun MainVideoScreen(
    query: () -> String,
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    data: () -> Videos,
    onPlayVideo: (Hits) -> Unit = {}
) {


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.imePadding()
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch
        )
        AnimatedVisibility(
            visible = data().hits.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyVideoResultScreen(text = query())
            if(query().isEmpty())
                onSearch()
        }
        AnimatedVisibility(
            visible = data().hits.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            VideoListScreen(
                data = data,
                onPlayVideo = onPlayVideo
            )
        }
    }

}

@Composable
fun VideoListScreen(
    data: () -> Videos,
    onPlayVideo: (Hits) -> Unit = {}
) {

    val lazyListState = rememberLazyListState()

    val videoData = remember(data()) {
        data()
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = videoData.hits,
            key = {
                it.id
            }
        ) {
            VideoCard(
                uri = it.videos.medium.url,
                width = it.videos.medium.width,
                height = it.videos.medium.height,
                onPlayVideo = { onPlayVideo(it) }
            )
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun SearchBar(
    query: () -> String = { "" },
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    context: Context = LocalContext.current
) {


    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query(),
        onValueChange = {
            onQueryChange(it)
        },
        shape = AbsoluteRoundedCornerShape(32.dp),
        placeholder = {
            Text(
                text = "Search Videos",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Videos"
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query().isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Erase Query"
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus(true)
                onSearch()
            }
        ),
        modifier = Modifier
            .onKeyEvent {
                if (it.key == Key.Back) {
                    focusManager.clearFocus(true)
                }
                true
            }
            .fillMaxWidth(1f)
            .padding(vertical = 4.dp, horizontal = 16.dp)


    )

}

@Preview(showSystemUi = true)
@Composable
fun VideoCard(
    context: Context = LocalContext.current,
    uri: String = "",
    width: Int = 0,
    height: Int = 0,
    onPlayVideo: () -> Unit = {}
) {

    var loadingState by remember {
        mutableStateOf(true)
    }

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
            .crossfade(2000)
            .crossfade(true)
            .build()
    }



    ElevatedCard(
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 8.dp,
            bottomStart = 8.dp,
            bottomEnd = 16.dp
        ),
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
            .clickable {
                onPlayVideo()
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp)

    ) {
        Box {
            AsyncImage(
                model = model,
                contentDescription = "Video Thumbnail",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .aspectRatio(
                        width.toFloat() / height.toFloat()
                    ),
                onSuccess = {
                    loadingState = false
                },

                )
            if (loadingState) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .align(Alignment.Center)
                )

            }
            if (!loadingState) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Click to play Video",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    tint = Color.White
                )
            }

        }
    }

}