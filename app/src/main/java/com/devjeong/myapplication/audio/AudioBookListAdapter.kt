package com.devjeong.myapplication.audio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.audio.model.Book
import com.devjeong.myapplication.databinding.AudioHomeItemBinding

class AudioBookListAdapter (
    private var books: List<Book>,
    private val itemClickListener: OnItemClickListener
    ) : RecyclerView.Adapter<AudioBookListAdapter.AudioBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AudioHomeItemBinding.inflate(inflater, parent, false)
        return AudioBookViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    override fun onBindViewHolder(holder: AudioBookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class AudioBookViewHolder(private val binding: AudioHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val book = books[position]
                    itemClickListener.onItemClick(book)
                }
            }
        }

        fun bind(book: Book) {
            binding.title = "${book.id}.${book.title}"
            binding.executePendingBindings()
        }
    }

    // 데이터가 변경될 때 호출하여 RecyclerView를 업데이트
    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
