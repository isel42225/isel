package isel.pdm.yama.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import isel.pdm.yama.Team
import isel.pdm.yama.activity.TeamOnClickListener
import isel.pdm.yama.viewmodel.TeamsViewModel

class TeamsViewHolder(view : CheckedTextView) : RecyclerView.ViewHolder(view){
    private val teamNameView : TextView = view.findViewById(android.R.id.text1)

    fun bindTo(team : Team, onClickListener : TeamOnClickListener){
        teamNameView.text = team.name
        onClickListener(team, teamNameView)
    }
}


class TeamsAdapter(private val vw : TeamsViewModel,
                   private val onClickListener : TeamOnClickListener): RecyclerView.Adapter<TeamsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(android.R.layout.simple_selectable_list_item, parent, false) as CheckedTextView
        )
    }



    override fun getItemCount(): Int  = vw.teams.value?.size ?:0

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        val teams = vw.teams.value
        if(teams != null)
            holder.bindTo(teams[position], onClickListener)
    }
}


