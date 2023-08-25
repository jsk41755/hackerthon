package com.devjeong.myapplication.community

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.UtilityBase
import com.devjeong.myapplication.community.model.paging.CommentPagingSourceFactory
import com.devjeong.myapplication.community.model.paging.CommunityChatViewModel
import com.devjeong.myapplication.community.model.paging.CommunityPagingAdapter
import com.devjeong.myapplication.community.model.paging.CommunityPagingRepository
import com.devjeong.myapplication.databinding.ActivityCommunityChatBinding
import kotlinx.coroutines.launch

class CommunityChatActivity : UtilityBase.BaseAppCompatActivity<ActivityCommunityChatBinding>(R.layout.activity_community_chat) {

    private lateinit var viewModel : CommunityChatViewModel
    private val communityPagingAdapter by lazy { CommunityPagingAdapter() }
    private var communityId: Int = -1
    private lateinit var inputMethodManager: InputMethodManager

    override fun ActivityCommunityChatBinding.onCreate(){
        val repository = CommunityPagingRepository()
        val viewModel: CommunityChatViewModel by viewModels { CommentPagingSourceFactory(repository) }
        this@CommunityChatActivity.viewModel = viewModel

        inputMethodManager = this@CommunityChatActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        communityId = intent.getIntExtra("communityId", -1)
        if (communityId != -1) {
            viewModel.searchPost(communityId)
        }

        binding.communityChatRv.adapter = communityPagingAdapter
        binding.communityChatRv.layoutManager = LinearLayoutManager(this@CommunityChatActivity, LinearLayoutManager.VERTICAL,false)
        //binding.communityChatRv.scrollToPosition(0)

        val imageResourceId = getImageResourceId(communityId)
        Glide.with(binding.celebProfile.context)
            .load(imageResourceId)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(binding.celebProfile)

        binding.btnSend.setOnClickListener {
            onCommentSubmitClicked()
        }

        viewModel.result.observe(this@CommunityChatActivity, Observer {
            communityPagingAdapter.submitData(lifecycle,it)
            Log.d("tst55", communityPagingAdapter.itemCount.toString())
            binding.communityChatRv.scrollToPosition(communityPagingAdapter.itemCount-1)
        })
    }

    private fun onCommentSubmitClicked() {
        val comment = binding.etMessage.text // 원하는 댓글 내용으로 변경
        inputMethodManager.hideSoftInputFromWindow(binding.etMessage.windowToken, 0)
        if (binding.etMessage.text.isNotEmpty()) {
            lifecycleScope.launch {
                viewModel.createComment(communityId, comment.toString())
                communityPagingAdapter.refresh()
            }
            binding.etMessage.text = null
        }
    }

    private fun getImageResourceId(id: Int): Int {
        return when (id) {
            1 -> R.drawable.jeongguk
            2 -> R.drawable.chaeunwoo
            3 -> R.drawable.vwe
            4 -> R.drawable.sugar
            5 -> R.drawable.yeji
            6 -> R.drawable.yuna
            7 -> R.drawable.imyoungwoong
            8 -> R.drawable.hani
            else -> R.drawable.daniel
        }
    }
}