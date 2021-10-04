package com.example.android.cats.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.cats.repositories.network.CatsAPI
import com.example.android.cats.repositories.network.VALUE_STARTING_PAGE_SIZE
import com.example.android.cats.repositories.network.pojo.CatItem
import com.example.android.cats.repositories.paing.CatsPagingSource
import kotlinx.coroutines.flow.Flow

class CatsRepository {
    private val apiService = CatsAPI.retrofitService

    fun getCats(): Flow<PagingData<CatItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = VALUE_STARTING_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CatsPagingSource(apiService) }
        ).flow
    }

    suspend fun getCatsForId(id: String): CatItem {
        return apiService.getCatsForId(id)
    }
}
