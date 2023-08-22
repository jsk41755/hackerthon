package com.devjeong.myapplication.community.model

data class CommunityApiResponse(
    val eventTime: String,
    val status: String,
    val code: Int,
    val data: List<CommunityData>
)

data class CommunityData(
    val communityId: Int,
    val celebrityName: String,
    val description: String,
    val url: String
)
