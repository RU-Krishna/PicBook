package com.feature.images.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import coil.request.ImageRequest
import com.common.error.emptyResult.EmptyImageResultScreen
import com.feature.images.R
import com.feature.images.model.Hits
import com.feature.images.model.Images

@Composable
fun ImageScreen(
    query: String = "",    //Query
    getImages: () -> Unit = {},  //Lambda for retrieving Images
    changeQuery: (String) -> Unit = {},   //Changing Query state
    images: Images,  //images result
    onImageClick: (Hits) -> Unit = {}  //What happens on clicking an Image.
) {

    //Top Level Column...
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        //Search Bar
        SearchBar(
            query = query,
            onSearch = getImages,
            changeQuery = changeQuery
        )


        AnimatedVisibility(
            visible = images.hits.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EmptyImageResultScreen(text = query)
            if(query.isEmpty())
                getImages()
        }

        //Image List
        AnimatedVisibility(
            visible = images.hits.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ImageList(
                images = images,
                onImageClick = onImageClick
            )
        }

    }


}


/*
* Outlined Text Field used as Search Bar...
*
* */
@Composable
fun SearchBar(
    query: String = "",
    onSearch: () -> Unit = {},
    changeQuery: (String) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = {
            changeQuery(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Button"
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus(true)
                onSearch()
            }
        ),
        singleLine = true,
        shape = AbsoluteRoundedCornerShape(32.dp),
        placeholder = {
            Text(
                text = "Search Images",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { changeQuery("") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Erase Query"
                    )
                }
            }
        },
        modifier = Modifier
            .onKeyEvent {
                if (it.key == Key.Back) {
                    focusManager.clearFocus(true)
                }
                true
            }
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)


    )


}

@Composable
fun ImageList(
    images: Images,
    onImageClick: (Hits) -> Unit = {}
) {

    val lazyListState = rememberLazyListState()


    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = images.hits, key = {
            it.id
        }) {
            ImagePreview(
                imageURL = it.imageURL,
                onImageClick = {
                    onImageClick(it)
                },
                imageWidth = it.imageWidth,
                imageHeight = it.imageHeight
            )
        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun ImagePreview(
    imageURL: String = "",
    onImageClick: () -> Unit = {},
    context: Context = LocalContext.current,
    imageWidth: Int = 0,
    imageHeight: Int = 0
) {

    var loadingState by remember {
        mutableStateOf(true)
    }

    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                onImageClick()
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageURL)
                    .crossfade(true)
                    .crossfade(2000)
                    .build(),
                contentDescription = "Flower",
                modifier = Modifier
                    .aspectRatio(imageWidth.toFloat() / imageHeight.toFloat()),
                contentScale = ContentScale.Fit,
                onSuccess = {
                    loadingState = false
                }
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
//            IconButton(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(end = 4.dp, bottom = 4.dp)
//                    .border(
//                        border = BorderStroke(
//                            width = 1.dp,
//                            color = Color.White
//                        ),
//                        shape = CircleShape
//                    )
//                    .size(32.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.Favorite,
//                    contentDescription = "Favorite",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(24.dp)
//                )
//            }

        }

    }
}


