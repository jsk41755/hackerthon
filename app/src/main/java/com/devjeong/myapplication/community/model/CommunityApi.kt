package com.devjeong.myapplication.community.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {
    @GET("/api/community")
    suspend fun getCommunityCeleb(): Response<CommunityApiResponse>
    @GET("/api/comment/paging/{communityId}")
    suspend fun getComments(
        @Path("communityId") communityId: Int,
        @Query("next") next: Int,
        @Query("size") size: Int
    ): Response<CommunityChatApiResponse>

    @POST("/api/comment/{communityId}")
    suspend fun createComment(
        @Path("communityId") communityId: Int,
        @Body request: CommentRequest
    ): Response<Void>
}
