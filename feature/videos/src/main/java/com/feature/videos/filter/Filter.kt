package com.feature.videos.filter

data class Filters(
    var videoType: String = "all",
    var editorsChoice: Boolean = false,
    var category: String = "",
    var order: String = "popular"
)

enum class FilterTypes {
    VideoType,
    EditorsChoice,
    Category,
    Order

}

enum class VideoType(val type: String) {
    ALL(type = "all"),
    FILM(type = "film"),
    ANIMATION(type = "animation")
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


enum class EditorsChoice(val value: Boolean) {
    TRUE(value = true),
    FALSE(value = false)
}

enum class Order(val order: String) {
    POPULAR(order = "popular"),
    LATEST(order = "latest")
}
