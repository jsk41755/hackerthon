package com.devjeong.myapplication.community

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.community.model.CommunityApiResponse
import com.devjeong.myapplication.community.model.CommunityData
import com.devjeong.myapplication.databinding.CommunityItemBinding

class CommunityAdapter : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    private val communityDataList = mutableListOf<CommunityData>()

    fun setData(dataList: List<CommunityData>) {
        communityDataList.clear()
        communityDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CommunityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val communityData = communityDataList[position]
        holder.bind(communityData)
    }

    override fun getItemCount(): Int {
        return communityDataList.size
    }

    inner class CommunityViewHolder(private val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CommunityData) {
            binding.celebName = data.celebrityName
            binding.celebNameTag = data.description

            val imageResourceId = getImageResourceId(data.communityId)
            Glide.with(binding.communityCelebIv.context)
                .load(imageResourceId)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(binding.communityCelebIv)

            binding.executePendingBindings()
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
            7 -> R.drawable.kyunglee
            8 -> R.drawable.hani
            else -> R.drawable.daniel
        }
    }
}

