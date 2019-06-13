package isel.pdm.yama.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import isel.pdm.yama.Profile
import isel.pdm.yama.R
import isel.pdm.yama.viewmodel.UserDetailsViewModel
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.utils.SharedPrefAccess
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : AppCompatActivity() {

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserDetailsViewModel(this@UserDetailsActivity.application as YamaApplication) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val profile = intent.getParcelableExtra<Profile>("profile")

        val org = intent.getStringExtra("org")
        val model = ViewModelProviders
                    .of(this, getViewModelFactory())
                    .get(UserDetailsViewModel::class.java)

        model.getAvatar(profile.avatar_url)
        model.image.observe(this, Observer { imageView.setImageBitmap(it) } )

        textViewLogin.text = profile.login
        textViewEmail.text = profile.email
        textViewFollowers.text = "${getString(R.string.Followers)}:${profile.followers}"
        val name = profile.name ?: ""
        textViewName.text = name
        buttonTeams.setOnClickListener{
            startActivity(Intent(this, TeamsActivity::class.java).putExtra("org", org))
        }
        logout.setOnClickListener{
            SharedPrefAccess(application).deleteSession()
            finish()
        }
    }

}
