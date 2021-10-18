package com.example.android.cats.repositories.network

import com.example.android.cats.BuildConfig

const val API_KEY = BuildConfig.API_KEY
const val BASE_URL = "https://api.thecatapi.com/v1/"
const val PARAMS_LIMIT = "limit"
const val PARAMS_PAGE = "page"
const val PARAMS_SIZE = "size"
const val PARAMS_HAS_BREEDS = "has_breeds"
const val PARAMS_INCLUDE_BREEDS = "include_breeds"

const val VALUE_LIMIT = 10
const val VALUE_SIZE_SMALL = "small"
const val VALUE_SIZE_THUMB = "thumb"
const val VALUE_STARTING_PAGE = 1
const val VALUE_STARTING_PAGE_SIZE = VALUE_LIMIT * 3
