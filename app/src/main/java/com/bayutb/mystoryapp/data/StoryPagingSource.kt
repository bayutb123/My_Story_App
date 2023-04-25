package com.bayutb.mystoryapp.data


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bayutb.mystoryapp.api.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, StoryList>() {
    override fun getRefreshKey(state: PagingState<Int,StoryList>): Int? {
        return state.anchorPosition?.let { position ->
            val anchor = state.closestPageToPosition(position)
            anchor?.prevKey?.plus(1) ?: anchor?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryList> {
        return try {
            val page = params.key ?: 1
            val responseData = apiService.fetchStories(token, params.loadSize, page)

            LoadResult.Page(
                data = responseData.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.items.isEmpty()) null else page + 1
            )

        } catch (e: java.lang.Exception) {
            return LoadResult.Error(e)
        }

    }

}
