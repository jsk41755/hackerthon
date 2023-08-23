package com.devjeong.myapplication.community.model.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.community.model.Comment
import com.devjeong.myapplication.databinding.CommunityChatItemBinding

class CommunityPagingAdapter
    :PagingDataAdapter<Comment, CommunityPagingAdapter.CommunityPagingViewHolder>(IMAGE_COMPARATOR){

    class CommunityPagingViewHolder(private val binding : CommunityChatItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(post : Comment){
            Log.d("tst5", "bind: ${post.id} 바인드됨")
            binding.communityChatTxt.text = post.comment
            binding.communityChatTimeTxt.text = post.createdAt
        }
    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPagingViewHolder {
        val binding = CommunityChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommunityPagingViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: CommunityPagingViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem == newItem
        }
    }
}