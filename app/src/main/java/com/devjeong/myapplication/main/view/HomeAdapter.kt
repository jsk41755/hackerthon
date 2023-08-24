package com.devjeong.myapplication.main.view

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.community.model.Rank
import com.devjeong.myapplication.databinding.HomePopularItemBinding
import com.devjeong.myapplication.databinding.RankItemBinding
import com.devjeong.myapplication.main.model.HomeBannerData

class HomeAdapter(private val itemList: List<HomeBannerData>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    inner class HomeViewHolder(private val binding: HomePopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HomeBannerData) {
            val spannableString = SpannableString(data.popularTitleTxt)

            val startIndex = data.popularTitleTxt.indexOf("*")
            val endIndex = startIndex + "*".length

            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(binding.root.context, R.color.myCelebHotPink))
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.popularTitleTxt.text = spannableString
            binding.popularTitleContent.text = data.popularTitleContent

            Glide.with(binding.popularImg.context)
                .load(data.popularImg)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(binding.popularImg)

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomePopularItemBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val popularItem = itemList[position]
        holder.bind(popularItem)
    }
}