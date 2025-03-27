package com.example.travl.items

data class MyPlansItem(
    val imageResURI: String = "",
    val placeName: String = "",
    val regionName: String = "",
    val key: String = ""
)

fun MyPlansItem.toMap(): Map<String, Any> {
    return mapOf(
        "imageResURI" to imageResURI,
        "placeName" to placeName,
        "regionName" to regionName,
        "key" to key
    )
}