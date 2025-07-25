package com.pvsrishabh.cakewake.features.home.data.remote.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pvsrishabh.cakewake.features.home.data.remote.api.HomeApiService
import com.pvsrishabh.cakewake.features.home.data.remote.dto.CakeDto
import java.io.IOException

class CakesPagingSource(
    private val apiService: HomeApiService,
    private val category: String? = null,
    private val budgetMin: Int? = null,
    private val budgetMax: Int? = null,
    private val search: String? = null
) : PagingSource<Int, CakeDto>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CakeDto> {
        return try {
            val page = params.key ?: 1
            val cakes = apiService.getCakes(
                page = page,
                limit = params.loadSize,
                category = category,
                budgetMin = budgetMin,
                budgetMax = budgetMax,
                search = search
            )

            LoadResult.Page(
                data = cakes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (cakes.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CakeDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
