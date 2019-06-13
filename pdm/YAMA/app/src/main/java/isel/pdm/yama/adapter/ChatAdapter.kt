package isel.pdm.yama.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import isel.pdm.yama.Message
import isel.pdm.yama.MessageBoard

class ChatViewHolder(view : TextView) : RecyclerView.ViewHolder(view) {
    private val msg  = view.findViewById<TextView>(android.R.id.text1)

    fun bindTo(m : Message?){
        msg.text = "${m?.sender} : ${m?.content}"
    }
}

class ChatAdapter(private val mb : MessageBoard ) : RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent , false) as TextView
        )
    }

    override fun getItemCount(): Int =  mb.messages.value?.size ?:0

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindTo(mb.messages.value?.get(position))
    }
}


