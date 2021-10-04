package com.example.android.cats.repositories.paing

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.cats.repositories.network.CatsApiService
import com.example.android.cats.repositories.network.VALUE_STARTING_PAGE
import com.example.android.cats.repositories.network.pojo.CatItem
import retrofit2.HttpException

class CatsPagingSource(
    private val service: CatsApiService
) : PagingSource<Int, CatItem>() {

    override fun getRefreshKey(state: PagingState<Int, CatItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatItem> {
        return try {
            val page = params.key ?: VALUE_STARTING_PAGE
            val limit = params.loadSize
            val listCats = service.getCats(limit = limit.toString(), page = page.toString())
            val nextPage = if (listCats.isEmpty()) null else page + 1
            val prevPage = if (page > 1) page - 1 else null
            LoadResult.Page(listCats, prevPage, nextPage)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
