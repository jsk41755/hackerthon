package com.devjeong.myapplication.audio

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.databinding.AudioBookItemBinding

class AudioBookScriptAdapter(private val contents: List<String>)
    : RecyclerView.Adapter<AudioBookScriptAdapter.AudioBookViewHolder>() {

    override fun onBindViewHolder(holder: AudioBookViewHolder, position: Int) {
        val content = contents[position]
        holder.bind(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AudioBookItemBinding.inflate(inflater, parent, false)
        return AudioBookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    class AudioBookViewHolder(private val binding: AudioBookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: String) {
            binding.script = content
            binding.executePendingBindings()
        }
    }
}

