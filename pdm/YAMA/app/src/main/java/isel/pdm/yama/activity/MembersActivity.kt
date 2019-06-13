package isel.pdm.yama.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import isel.pdm.yama.Profile
import isel.pdm.yama.R
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.adapter.MembersAdapter
import isel.pdm.yama.utils.SharedPrefAccess
import isel.pdm.yama.viewmodel.MembersViewModel
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.content_members.*

class MembersActivity : AppCompatActivity() {

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MembersViewModel(this@MembersActivity.application as YamaApplication) as T
        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        setSupportActionBar(toolbar)

        val prefAccess = SharedPrefAccess(this)

        val accessToken = prefAccess.getToken()
        val idTeam = intent.getIntExtra("id", -1)
        val teamName = intent.getStringExtra("name")
        toolbar_layout.title = teamName
        membersView.setHasFixedSize(true)
        membersView.layoutManager = LinearLayoutManager(this)

        val model = ViewModelProviders
            .of(this, getViewModelFactory())
            .get(MembersViewModel::class.java)

        val url = "https://api.github.com/teams/$idTeam/members?access_token=$accessToken"
        model.getMembers(url, idTeam)

        model.members.observe(this, Observer<Array<Profile>> { profileArray ->
            membersView.adapter = MembersAdapter(model)
        })

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        fab.setOnClickListener {
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork?.isConnected == true
            if(isConnected)
                startActivity(Intent(this, ChatActivity::class.java)
                                .putExtra("team", teamName))
            else
                Toast
                    .makeText(this, getString(R.string.network_warning), Toast.LENGTH_LONG)
                    .show()
        }
    }
}
