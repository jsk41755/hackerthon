package com.devjeong.myapplication.community.model.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class CommunityChatViewModel (private val repository : CommunityPagingRepository) : ViewModel() {

    private val _communityCustomPosts = MutableLiveData<Int>()
    val communityCustomPosts: LiveData<Int> = _communityCustomPosts

    private val _commentCreationResult = MutableLiveData<Boolean>()
    val commentCreationResult: LiveData<Boolean> = _commentCreationResult

    // 라이브 데이터 변경 시 다른 라이브 데이터 발행
    val result = _communityCustomPosts.switchMap { queryString ->
        repository.getPost(queryString).cachedIn(viewModelScope)
    }

    // 라이브 데이터 변경
    fun searchPost(userId: Int) {
        _communityCustomPosts.value = userId
        Log.d("tst56", _communityCustomPosts.value.toString())
    }

    suspend fun createComment(communityId: Int, comment: String) {
        try {
            val response = repository.createComment(communityId, comment)
            _commentCreationResult.postValue(response.isSuccessful)
        } catch (e: Exception) {
            _commentCreationResult.postValue(false)
        }
    }

}