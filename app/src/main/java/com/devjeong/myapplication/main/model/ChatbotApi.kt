package com.devjeong.myapplication.main.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatbotApi {
    @GET("/chatbot/b")
    fun getKobertResponse(
        @Query("s") s:String
    ): Call<ChatbotDto>
}