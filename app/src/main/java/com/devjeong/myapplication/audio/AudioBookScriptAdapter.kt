package com.devjeong.myapplication.audio

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.R
import com.devjeong.myapplication.databinding.AudioBookItemBinding

class AudioBookScriptAdapter(
    private var contents: List<String>,
    private val playCallback: (Int) -> Unit
) : RecyclerView.Adapter<AudioBookScriptAdapter.AudioBookViewHolder>() {

    override fun onBindViewHolder(holder: AudioBookViewHolder, position: Int) {
        val content = contents[position]
        holder.bind(content, position)
        holder.itemView.setOnClickListener {
            playCallback.invoke(position)
        }
    }

    fun updateContents(newContents: List<String>) {
        contents = newContents
        notifyDataSetChanged()
    }

    private var playingPosition: Int = -1

    fun setPlayingPosition(position: Int) {
        val previousPosition = playingPosition
        playingPosition = position
        // Notify the adapter to update the views for the previous and current items
        if (previousPosition != -1) {
            notifyItemChanged(previousPosition)
        }
        if (playingPosition != -1) {
            notifyItemChanged(playingPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AudioBookItemBinding.inflate(inflater, parent, false)
        return AudioBookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    fun getItem(position: Int): String {
        return contents[position]
    }

    inner class AudioBookViewHolder(private val binding: AudioBookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: String, position: Int) {
            binding.script = content
            binding.executePendingBindings()

            binding.audioBookScript.setTextColor(
                if (position == playingPosition) {
                    ContextCompat.getColor(itemView.context, R.color.white).also {
                        binding.audioBookScript.setTypeface(null, Typeface.BOLD)
                    }
                } else {
                    ContextCompat.getColor(itemView.context, R.color.imy_text_gray).also {
                        binding.audioBookScript.setTypeface(null, Typeface.NORMAL)
                    }
                }
            )
        }
    }
}

