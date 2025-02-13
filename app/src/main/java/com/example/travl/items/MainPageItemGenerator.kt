package com.example.travl.items

import com.example.travl.R

object MainPageItemGenerator {
    private val imageId: Int = R.drawable.place


    fun generateMainPageItem(count: Int): List<MainPageChildItem> =
        (0..count).map { it ->
            MainPageChildItem(
                imageResId = imageId,
                placeName = "Место №$it",
                regionName = "Регион №$it",
                score = "${it % 6}.${it % 10}"
            )
        }

}
///Временная заглушка до подтягивания данных из БД