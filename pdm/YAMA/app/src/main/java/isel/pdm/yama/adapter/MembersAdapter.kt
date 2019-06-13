package isel.pdm.yama.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import isel.pdm.yama.Profile
import isel.pdm.yama.viewmodel.MembersViewModel

class MembersViewHolder(view : CheckedTextView) : RecyclerView.ViewHolder(view){
    private val memberNameView : TextView = view.findViewById(android.R.id.text1)

    fun bindTo(member : Profile){
        memberNameView.text = member.login
    }
}

class MembersAdapter(private val vw : MembersViewModel): RecyclerView.Adapter<MembersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder =
        MembersViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(android.R.layout.simple_selectable_list_item, parent, false) as CheckedTextView
        )

    override fun getItemCount(): Int = vw.members.value?.size ?: 0

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val members = vw.members.value
        if(members != null)
            holder.bindTo(members[position])
    }

}