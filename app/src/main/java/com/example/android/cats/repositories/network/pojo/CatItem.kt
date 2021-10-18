package com.example.android.cats.repositories.network.pojo

import com.squareup.moshi.Json

data class CatItem(
    @Json(name = "breeds")
    var breeds: List<BreedItem>? = null,
    @Json(name = "id")
    var id: String? = "",
    @Json(name = "url")
    var url: String? = ""
)
