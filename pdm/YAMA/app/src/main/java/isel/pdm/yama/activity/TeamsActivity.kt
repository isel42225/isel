package isel.pdm.yama.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import isel.pdm.yama.R
import isel.pdm.yama.Team
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.adapter.TeamsAdapter
import isel.pdm.yama.viewmodel.TeamsViewModel
import kotlinx.android.synthetic.main.activity_teams.*
import kotlinx.android.synthetic.main.content_teams.*


typealias TeamOnClickListener  = (team : Team, tv : TextView) -> Unit

class TeamsActivity : AppCompatActivity() {

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TeamsViewModel(this@TeamsActivity.application as YamaApplication) as T
        }
    }

    private fun registerTeamOnClick(team : Team, tv :TextView) {
        tv.setOnClickListener {
            val newActivity = Intent(this, MembersActivity::class.java)
                .putExtra("id", team.id)
                .putExtra("name", team.name)
            startActivity(newActivity)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)
        setSupportActionBar(toolbar)

        val org = intent.getStringExtra("org")

        toolbar_layout.title = org
        teamsView.setHasFixedSize(true)
        teamsView.layoutManager = LinearLayoutManager(this)

        val model = ViewModelProviders
                        .of(this, getViewModelFactory())
                        .get(TeamsViewModel::class.java)


        teamsView.adapter =
                TeamsAdapter(model, this::registerTeamOnClick)


        model.teams.observe(this, Observer<Array<Team>>{
            teamsView.adapter =
                    TeamsAdapter(model, this::registerTeamOnClick)
        })

        model.getTeamsFromDB(org)

        }

    }

