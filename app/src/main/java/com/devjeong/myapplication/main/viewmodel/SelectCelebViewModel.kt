package com.devjeong.myapplication.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjeong.myapplication.community.model.CommunityClient
import com.devjeong.myapplication.community.model.CountRequest
import com.devjeong.myapplication.community.model.Rank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectCelebViewModel : ViewModel() {

    private val _selectedCelebName = MutableStateFlow("")
    val selectedCelebName: StateFlow<String> = _selectedCelebName

    private val _selectedCelebNum = MutableStateFlow(0)
    val selectedCelebNum: StateFlow<Int> = _selectedCelebNum

    private val _selectedCelebKor = MutableStateFlow("")
    val selectedCelebKor: StateFlow<String> = _selectedCelebKor

    private val _rankList = MutableStateFlow<List<Rank>>(emptyList())
    val rankList: StateFlow<List<Rank>> = _rankList

    fun updateSelectedCelebName(celebName: String) {
        _selectedCelebName.value = celebName
    }

    fun fetchRankData() {
        viewModelScope.launch {
            try {
                val response = CommunityClient.communityApi.getRank()
                Log.d("fetchRankData", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    val rankApiResponse = response.body()
                    val rankDataList = rankApiResponse?.data ?: emptyList()
                    _rankList.value = rankDataList
                } else {
                    Log.d("fetchRankData", response.isSuccessful.toString())
                }
            } catch (e: Exception) {
                Log.d("fetchRankData", e.toString())
            }
        }
    }

    fun fetchCelebNum(celebName: String): Int {
        viewModelScope.launch {
            _selectedCelebNum.value = when (celebName) {
                "CHAEUNWOO" -> 2
                "JEONGGUK" -> 1
                "VWE" -> 3
                "SUGAR" -> 4
                "YEJI" -> 5
                "YUNA" -> 6
                "KYUNGLEE" -> 7
                "HANI" -> 8
                else -> 9
            }
        }

        return selectedCelebNum.value
    }

    fun fetchCelebKor(celebName: Int): String {
        viewModelScope.launch {
            _selectedCelebKor.value = when (celebName) {
                1 -> "정국"
                2 -> "차은우"
                3 -> "뷔"
                4 -> "슈가"
                5 -> "예지"
                6 -> "유나"
                7 -> "경리"
                8 -> "팜하니"
                else -> "다니엘"
            }
        }
        return selectedCelebKor.value
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

