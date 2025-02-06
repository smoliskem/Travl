package com.example.travl.items

import com.example.travl.R

object MainPageItemGenerator {
    private val imageId: Int = R.drawable.place


    fun generateMainPageItem(count: Int): List<MainPageItem> =
        (0..count).map { it ->
            MainPageItem(
                imageResId = imageId,
                placeName = "Место №$it",
                regionName = "Регион №$it",
                score = "${it % 6}.${it % 10}"
            )
        }

}