package com.devjeong.myapplication.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjeong.myapplication.community.model.CommunityApiResponse
import com.devjeong.myapplication.community.model.CommunityClient
import com.devjeong.myapplication.community.model.CommunityData
import com.devjeong.myapplication.community.model.CountRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityViewModel : ViewModel() {

    private val _communityData = MutableStateFlow<List<CommunityData>>(emptyList())
    val communityData: StateFlow<List<CommunityData>> = _communityData

    fun fetchCommunityData() {
        viewModelScope.launch {
            try {
                val response = CommunityClient.communityApi.getCommunityCeleb()
                if (response.isSuccessful) {
                    val communityDataList = response.body()?.data ?: emptyList()
                    _communityData.emit(communityDataList)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun performCountRequest(macId: String, celebId: Int) {
        val countRequest = CountRequest(macId, celebId)
        val call: Call<Unit> = CommunityClient.communityApi.getCount(countRequest)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // 요청 성공 시 처리
                } else {
                    // 요청 실패 시 처리
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // 네트워크 오류 등 처리
            }
        })
    }

}


