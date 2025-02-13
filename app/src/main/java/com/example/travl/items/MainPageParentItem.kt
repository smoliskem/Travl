package com.example.travl.items

data class MainPageParentItem(
    val childItemList: List<MainPageChildItem>,
    val regionName: String
)