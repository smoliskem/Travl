package com.example.travl.items

data class MyPlansItem(
    val imageResURI: String = "",
    val placeName: String = "",
    val regionName: String = "",
    val description: String = "",
    val key: String = ""
)

fun MyPlansItem.toMap(): Map<String, Any> {
    return mapOf(
        "imageResURI" to imageResURI,
        "placeName" to placeName,
        "regionName" to regionName,
        "description" to description,
        "key" to key
    )
}