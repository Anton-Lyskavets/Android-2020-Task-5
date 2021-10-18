package com.example.android.cats.repositories.network

import com.example.android.cats.repositories.network.pojo.CatItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApiService {
    @GET("images/search")
    suspend fun getCats(
        @Query(PARAMS_LIMIT) limit: String = VALUE_LIMIT.toString(),
        @Query(PARAMS_PAGE) page: String = VALUE_STARTING_PAGE.toString(),
        @Query(PARAMS_SIZE) size: String = VALUE_SIZE_SMALL,
        @Query(PARAMS_INCLUDE_BREEDS) include_breeds: String = "false",
        @Query(PARAMS_HAS_BREEDS) has_breeds: String = "true"
    ): List<CatItem>

    @GET("images/{image_id}")
    suspend fun getCatsForId(
        @Path("image_id") image_id: String,
        @Query(PARAMS_SIZE) size: String = VALUE_SIZE_THUMB,
        @Query(PARAMS_INCLUDE_BREEDS) include_breeds: String = "true",
        @Query(PARAMS_HAS_BREEDS) has_breeds: String = "true"
    ): CatItem
}
