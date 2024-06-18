package com.example.litfinder.remote.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.litfinder.remote.api.ApiResponseStatus
import com.example.litfinder.remote.api.ApiService
import com.example.litfinder.remote.pref.UserPreferences
import com.example.litfinder.remote.repository.Repository
import com.example.litfinder.remote.response.BookItem
import com.example.litfinder.remote.response.BookResponse
import kotlinx.coroutines.flow.first

class BookPagingSource(
    private val pref: UserPreferences,
    private val apiService: ApiService,
    private val searchQuery: String?
) : PagingSource<Int, BookItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.getUser().first().token
            if (token.isNotEmpty()) {
                val responseData = apiService.getBooks(token, params.loadSize, page, searchQuery)
                if (responseData.status == "success") {
                    val sortedData = responseData.data?.sortedByDescending {
                        Log.d("PublishedDateBookPagingSource", "Published Date: ${it.publishedDate}")
                        it.publishedDate
                    } ?: emptyList()
                    LoadResult.Page(
                        data = sortedData,
                        prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                        nextKey = if (responseData.data.isNullOrEmpty()) null else page + 1
                    )
                } else {
                    LoadResult.Error(Exception("Failed to load books"))
                }
            } else {
                LoadResult.Error(Exception("Token is empty"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, BookItem>): Int? {
        return null
    }
}
