package com.example.android.cats.repositories.network.pojo

import com.squareup.moshi.Json

data class BreedItem(
    @Json(name = "id")
    var id: String?,
    @Json(name = "name")
    var name: String?,
    @Json(name = "temperament")
    var temperament: String?,
    @Json(name = "origin")
    var origin: String?,
    @Json(name = "description")
    var description: String?,
    @Json(name = "life_span")
    var lifeSpan: String?,
    @Json(name = "adaptability")
    var adaptability: Int?,
    @Json(name = "child_friendly")
    var childFriendly: Int?,
    @Json(name = "grooming")
    var grooming: Int?,
    @Json(name = "health_issues")
    var healthIssues: Int?,
    @Json(name = "intelligence")
    var intelligence: Int?,
    @Json(name = "stranger_friendly")
    var strangerFriendly: Int?,
    @Json(name = "wikipedia_url")
    var wikipediaUrl: String?
)
