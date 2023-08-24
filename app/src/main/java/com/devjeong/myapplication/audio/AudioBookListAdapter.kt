package com.devjeong.myapplication.audio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.R
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
            when(book.title){
                "마지막 잎새" -> binding.audioBookImg.setImageResource(R.drawable.last_leaf)
                "메밀꽃 필 무렵" -> binding.audioBookImg.setImageResource(R.drawable.buckwheat_flowers)
                "들사람 얼" -> binding.audioBookImg.setImageResource(R.drawable.wild_man)
                "나의 사랑 한글날" -> binding.audioBookImg.setImageResource(R.drawable.hangul)
                "별" -> binding.audioBookImg.setImageResource(R.drawable.star_book)
                "부자와 당나귀" -> binding.audioBookImg.setImageResource(R.drawable.rich_and_the_donkey)
                "소나기" -> binding.audioBookImg.setImageResource(R.drawable.shower)
                "어린 왕자" -> binding.audioBookImg.setImageResource(R.drawable.little_prince)
                "청산도" -> binding.audioBookImg.setImageResource(R.drawable.degree_of_liquidation)
                "동백꽃" -> binding.audioBookImg.setImageResource(R.drawable.camellia_flower)
                "별 헤는 밤" -> binding.audioBookImg.setImageResource(R.drawable.night_of_counting_the_stars)
            }
            binding.executePendingBindings()
        }
    }

    // 데이터가 변경될 때 호출하여 RecyclerView를 업데이트
    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
