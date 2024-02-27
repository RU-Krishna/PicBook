package com.feature.images.filters

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    modalBottomSheetState: SheetState,
    hideBottomSheet: () -> Unit,
    filters: Filters,
    applyFilter: () -> Unit,
    resetFilter: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    var selectedFilterType by remember {
        mutableStateOf("Category")
    }

    ModalBottomSheet(
        onDismissRequest = {
            hideBottomSheet()
        },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
            securePolicy = SecureFlagPolicy.SecureOn,
            isFocusable = false
        ),
        modifier = Modifier.wrapContentSize(),
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .wrapContentSize()
            ) {
                HorizontalDivider(
                    thickness = 4.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier
                        .width(64.dp)
                )
            }
        }
    ) {

        BackHandler {
            hideBottomSheet()
        }


        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                text = "Filters",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 2.dp)

            )
            HorizontalDivider(thickness = 2.dp)
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(0.8f)
            ) {
                FilterScreenLeftPane(
                    selectedFilterType = selectedFilterType,
                    changeFilterType = {
                        selectedFilterType = it
                    },
                    modifier = Modifier.weight(0.4f)
                )
                VerticalDivider(thickness = 2.dp)
                FilterScreenRightPane(
                    selectedFilterType = selectedFilterType,
                    modifier = Modifier.weight(0.60f),
                    filters = filters
                )

            }
            HorizontalDivider(thickness = 2.dp)
            FilterButtons(
                hideBottomSheet = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                        .invokeOnCompletion {
                            hideBottomSheet()
                        }
                },
                applyFilter = applyFilter,
                resetFilter = resetFilter
            )
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun FilterScreenLeftPane(
    modifier: Modifier = Modifier,
    selectedFilterType: String = "",
    changeFilterType: (String) -> Unit = {},
) {


    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(4.dp)
    ) {
        FilterTypes.entries.forEach {
            TextButton(
                onClick = { changeFilterType(it.name) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedFilterType == it.name)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                border = BorderStroke(width = 1.dp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Text(
                    text = it.name,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                )
            }
        }

    }
}

@Composable
fun FilterScreenRightPane(
    modifier: Modifier = Modifier,
    selectedFilterType: String = "",
    filters: Filters
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentSize()
    ) {

        AnimatedContent(targetState = selectedFilterType, label = "") {
            when (it) {

                FilterTypes.ImageType.name -> {
                    ImageTypeFilter(filters)
                }

                FilterTypes.Orientation.name -> {
                    OrientationFilter(filters)
                }

                FilterTypes.EditorsChoice.name -> {
                    EditorsChoiceFilter(filters)
                }

                FilterTypes.Category.name -> {
                    CategoryFilter(filters)
                }

                FilterTypes.Color.name -> {
                    ColorFilter(filter = filters)
                }

                FilterTypes.Order.name -> {
                    OrderFilter(filters)
                }
            }
        }
    }

}

@Composable
fun FilterButtons(
    hideBottomSheet: () -> Unit,
    applyFilter: () -> Unit,
    resetFilter: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(
            onClick = {
                hideBottomSheet()
                resetFilter()

            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .weight(0.4f),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline)

        ) {
            Text(text = "Reset")
        }
        FilledTonalButton(
            onClick = {
                hideBottomSheet()
                applyFilter()

            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .weight(0.6f),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)


        ) {
            Text(text = "Apply")
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showSystemUi = true)
@Composable
fun ImageTypeFilter(filter: Filters = Filters()) {

    var selectedItem by remember {
        mutableStateOf(filter.imageType)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)

    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ImageType.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.type,
                    onClick = {
                        filter.imageType = it.type
                        selectedItem = it.type
                    },
                    label = {
                        Text(text = it.toString())
                    })
            }
        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrientationFilter(filter: Filters) {

    var selectedItem by remember {
        mutableStateOf(filter.orientation)
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)

    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Orientation.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.type,
                    onClick = {
                        filter.orientation = it.type
                        selectedItem = it.type
                    },
                    label = {
                        Text(text = it.name)
                    })
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showSystemUi = true)
@Composable
fun CategoryFilter(filter: Filters = Filters()) {

    var selectedItem by remember {
        mutableStateOf(filter.category)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)

    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .padding(4.dp)
        ) {
            Category.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.cname,
                    onClick = {
                        filter.category = it.cname
                        selectedItem = it.cname
                    },
                    label = {
                        Text(text = it.name)
                    })
            }
        }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Preview(showSystemUi = true)
@Composable
fun ColorFilter(modifier: Modifier = Modifier, filter: Filters = Filters()) {

    var selectedItem by remember {
        mutableStateOf(filter.color)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)
    ) {
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .padding(4.dp)
        ) {
            Colors.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.color,
                    onClick = {
                        filter.color = it.color
                        selectedItem = it.color
                    },
                    label = {
                        Text(
                            text = it.name,
                            color = if (it.color == Colors.Transparent.color || it.color == Colors.White.color)
                                Color.Black
                            else
                                Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = it.code
                    )

                )
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showSystemUi = true)
@Composable
fun EditorsChoiceFilter(filter: Filters = Filters()) {

    var selectedItem by remember {
        mutableStateOf(filter.editorsChoice)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)

    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            EditorsChoice.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.value,
                    onClick = {
                        filter.editorsChoice = it.value
                        selectedItem = it.value
                    },
                    label = {
                        Text(text = it.name)
                    })
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showSystemUi = true)
@Composable
fun OrderFilter(filter: Filters = Filters()) {

    var selectedItem by remember {
        mutableStateOf(filter.order)
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 2.dp)

    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Order.entries.forEach {
                FilterChip(
                    selected = selectedItem == it.order,
                    onClick = {
                        filter.order = it.order
                        selectedItem = it.order
                    },
                    label = {
                        Text(text = it.name)
                    })
            }
        }

    }
}

