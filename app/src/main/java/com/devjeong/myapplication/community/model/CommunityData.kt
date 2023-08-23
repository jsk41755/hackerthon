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

data class CommunityChatApiResponse(
    val eventTime: String,
    val status: String,
    val code: Int,
    val data: CommunityResponseData
    )
data class CommunityResponseData(
    val values: List<Comment>,
    val hasNext: Boolean,
    val lastIndex: Int
    )
data class Comment(
    val id: Int,
    val comment: String,
    val createdAt: String
    )
data class CommentRequest(
    val comment: String
    )
data class CountRequest(
    val from: String,
    val to: Int
)

