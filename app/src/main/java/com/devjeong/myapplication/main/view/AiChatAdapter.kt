package com.devjeong.myapplication.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devjeong.myapplication.R
import com.devjeong.myapplication.main.model.Message
import com.devjeong.myapplication.util.Constants.RECEIVE_ID
import com.devjeong.myapplication.util.Constants.SEND_ID

class AiChatAdapter : RecyclerView.Adapter<AiChatAdapter.AiMessageViewHolder>() {

    var messagesList = mutableListOf<Message>()

    inner class AiMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        val tvBotMessage: TextView = itemView.findViewById(R.id.tv_bot_message)
        var currentMessage: Message? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiMessageViewHolder {
        return AiMessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    fun setMessages(messages: MutableList<Message>) {
        messagesList = messages
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AiMessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.tvMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tvBotMessage.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.tvBotMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tvMessage.visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }
}
