package com.devjeong.myapplication.community.model

import retrofit2.Response
import retrofit2.http.GET

interface CommunityApi {
    @GET("/api/community")
    suspend fun getCommunityCeleb(): Response<CommunityApiResponse>
}
