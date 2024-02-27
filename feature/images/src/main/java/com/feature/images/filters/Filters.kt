package com.feature.images.filters

import androidx.compose.ui.graphics.Color

data class Filters(
    var imageType: String = "all",
    var orientation: String = "all",
    var editorsChoice: Boolean = false,
    var category: String = "",
    var color: String = "",
    var order: String = "popular"
)

enum class FilterTypes {
    ImageType,
    Orientation,
    EditorsChoice,
    Category,
    Color,
    Order

}

enum class ImageType(val type: String) {
    ALL(type = "all"),
    PHOTO(type = "photo"),
    ILLUSTRATION(type = "illustration"),
    VECTOR(type = "vector")
}

enum class Orientation(val type: String) {
    ALL(type = "all"),
    HORIZONTAL(type = "horizontal"),
    VERTICAL(type = "vertical")
}

enum class Category(val cname: String) {
    Background(cname = "backgrounds"),
    Fashion(cname = "fashion"),
    Nature(cname = "nature"),
    Science(cname = "science"),
    Education(cname = "education"),
    Feelings(cname = "feelings"),
    Health(cname = "health"),
    People(cname = "people"),
    Religion(cname = "religion"),
    Places(cname = "places"),
    Animals(cname = "animals"),
    Industry(cname = "industry"),
    Computer(cname = "computer"),
    Food(cname = "food"),
    Sports(cname = "sports"),
    Transportation(cname = "transportation"),
    Travel(cname = "travel"),
    Buildings(cname = "buildings"),
    Business(cname = "business"),
    Music(cname = "music")
}

enum class Colors(val code: Color, val color: String) {

    Grayscale(code = Color(0xFF808080), color = "grayscale"),
    Transparent(code = Color.Transparent, color = "transparent"),
    Red(code = Color.Red, color = "red"),
    Orange(code = Color(0xFFffa500), color = "orange"),
    Yellow(code = Color.Yellow, color = "yellow"),
    Green(code = Color.Green, color = "green"),
    Torquoise(code = Color(0xFF40e0d0), color = "torquoise"),
    Blue(code = Color.Blue, color = "blue"),
    Lilac(code = Color(0xFFc8a2c8), color = "lilac"),
    Pink(code = Color(0xFFffc0cb), color = "pink"),
    White(code = Color.White, color = "white"),
    Gray(code = Color.Gray, color = "gray"),
    Black(code = Color.Black, color = "black"),
    Brown(code = Color(0xFFa52a2a), color = "brown")

}

enum class EditorsChoice(val value: Boolean) {
    TRUE(value = true),
    FALSE(value = false)
}

enum class Order(val order: String) {
    POPULAR(order = "popular"),
    LATEST(order = "latest")
}
