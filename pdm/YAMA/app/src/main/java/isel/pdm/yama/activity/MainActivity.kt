package isel.pdm.yama.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import isel.pdm.yama.Profile
import isel.pdm.yama.R
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.model.YamaDB
import isel.pdm.yama.utils.SharedPrefAccess
import isel.pdm.yama.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(this@MainActivity.application as YamaApplication) as T
        }
    }

    private var remember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefAccess = SharedPrefAccess(application)
        val model = ViewModelProviders
            .of(this, getViewModelFactory())
            .get(LoginViewModel::class.java)


        button_login.setOnClickListener {
            val token = prefAccess.getToken()
            val login = prefAccess.getLogin()
            val org = prefAccess.getOrg()

            if(token != "" && login != "" && org != "" ) {
                remember = true
                //val urlLogin = "https://api.github.com/user?access_token=$token"
                //model.attemptLogin(urlLogin)
                model.getLoggedUser(login)
            }else{
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            }
        }

        model.profile.observe(this, Observer<Profile> { profile ->
            if(remember){
                val userDetails = Intent(this, UserDetailsActivity::class.java)
                val org = prefAccess.getOrg()
                userDetails
                    .putExtra("profile", profile )
                    .putExtra("org", org)
                startActivity(userDetails)
            }
        })
    }
}
