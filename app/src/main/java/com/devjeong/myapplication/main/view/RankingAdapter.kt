package com.devjeong.myapplication.main.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devjeong.myapplication.R
import com.devjeong.myapplication.community.model.Rank
import com.devjeong.myapplication.databinding.RankItemBinding

class RankingAdapter(private val rankDataList: List<Rank>, private val rankPointList: Array<String>) : RecyclerView.Adapter<RankingAdapter.RankViewHolder>() {

    inner class RankViewHolder(private val binding: RankItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Rank) {
            binding.rankItemNum.text = data.rank.toString()
            binding.tvCelebName.text = data.celebrityName
            binding.tvCelebDescription.text = data.celebrityDescription
            binding.rankItemTrash.text = rankPointList[data.celebrityId-1]

            if (data.rankMovement > 0){
                binding.rankItemVariance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.popularity_up, 0, 0, 0)
                binding.rankItemVariance.text = data.rankMovement.toString()
                binding.rankItemVariance.setTextColor(Color.RED)
            } else if(data.rankMovement == 0){
                binding.rankItemVariance.text = "-"
                binding.rankItemVariance.setTextColor(ContextCompat.getColor(binding.root.context, R.color.imy_chart_gray))
            } else{
                var minus = data.rankMovement.toString()
                minus = minus.replace("-","")
                binding.rankItemVariance.text = minus
                binding.rankItemVariance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.popularity_down, 0, 0, 0)
                binding.rankItemVariance.setTextColor(Color.BLUE)
            }

            val imageResourceId = getImageResourceId(data.celebrityId)
            Glide.with(binding.celebProfile.context)
                .load(imageResourceId)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .into(binding.celebProfile)

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RankItemBinding.inflate(inflater, parent, false)
        return RankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val rankItem = rankDataList[position]
        holder.bind(rankItem)
    }

    override fun getItemCount(): Int {
        return rankDataList.size
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