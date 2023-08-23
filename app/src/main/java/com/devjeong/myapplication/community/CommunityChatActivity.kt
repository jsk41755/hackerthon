package com.devjeong.myapplication.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
}