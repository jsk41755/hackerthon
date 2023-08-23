package com.devjeong.myapplication.community

import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_chat)

        binding.communityChatRv.adapter = communityPagingAdapter
        binding.communityChatRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.communityChatRv.scrollToPosition(0)

        val repository = CommunityPagingRepository()
        val viewModelFactory = CommentPagingSourceFactory(repository)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[CommunityChatViewModel::class.java]

        viewModel.searchPost(2)

        binding.btnSend.setOnClickListener {
            onCommentSubmitClicked()
        }

        viewModel.result.observe(this, Observer {
            communityPagingAdapter.submitData(this.lifecycle,it)
            Log.d("tst55", "호출됐음.")
        })
    }

    private fun onCommentSubmitClicked() {
        val comment = binding.etMessage.text // 원하는 댓글 내용으로 변경
        if (binding.etMessage.text.isNotEmpty()) {
            val communityId = 2 // 원하는 커뮤니티 ID
            lifecycleScope.launch {
                viewModel.createComment(communityId, comment.toString())
                communityPagingAdapter.refresh()
            }
            binding.etMessage.text = null
        }
    }
}