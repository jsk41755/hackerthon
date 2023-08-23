package com.devjeong.myapplication.community.model.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.devjeong.myapplication.community.model.CommentRequest
import com.devjeong.myapplication.community.model.CommunityClient
import retrofit2.Response

class CommunityPagingRepository {
    fun getPost(userId : Int) =
        Pager(
            config = PagingConfig(
                pageSize = 3,
                maxSize = 10,
                enablePlaceholders = false
            ),
            // 사용할 메소드 선언
            pagingSourceFactory = { CommunityPagingSource(CommunityClient.communityApi,userId)}
        ).liveData

    suspend fun createComment(communityId: Int, comment: String): Response<Void> {
        return CommunityClient.communityApi.createComment(communityId, CommentRequest(comment))
    }
}