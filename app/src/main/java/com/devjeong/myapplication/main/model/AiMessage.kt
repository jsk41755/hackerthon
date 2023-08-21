package com.devjeong.myapplication.main.model

data class AiMessage(
    val answer: String,
    val category: Int,
    val category_info: String
)

data class Message(
    val message: String,
    val id: String,
    val time: String,
    var isLiked: Boolean
)