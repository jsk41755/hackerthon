package com.devjeong.myapplication.community.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devjeong.myapplication.community.model.Comment
import com.devjeong.myapplication.community.model.CommunityApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 100

class CommunityPagingSource(
    private val communityApi : CommunityApi,
    private val communityId : Int
) : PagingSource<Int, Comment>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val nextPosition = position + 1

        return try {
            val response = communityApi.getComments(
                communityId = communityId,
                next = position,
                size = params.loadSize
            )
            val post = response.body()?.data

            LoadResult.Page(
                data = post?.values?.reversed() ?: emptyList(),  // 내림차순
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (post?.hasNext == true) nextPosition + 1 else null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return STARTING_PAGE_INDEX
    }
}

