package isel.pdm.yama.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import isel.pdm.yama.Profile
import isel.pdm.yama.R
import isel.pdm.yama.Team
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.utils.SharedPrefAccess
import isel.pdm.yama.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()  {

    private var logon = false

    private fun getViewModelFactory() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(this@LoginActivity.application as YamaApplication) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefAccess = SharedPrefAccess(application)

        val userSuggestions = prefAccess.getPrefLogin(this, editUser)
        val orgSuggestions =  prefAccess.getPrefOrg(this, editOrg)

        val model = ViewModelProviders
            .of(this, getViewModelFactory())
            .get(LoginViewModel::class.java)

        model.profile.observe(this, Observer<Profile> { profile ->

            if(profile?.login == editUser.text.toString()) {
                val login  = profile.login

                val accessToken = editAccessToken.text.toString()
                val org = editOrg.text.toString()
                model.verifyOrg("https://api.github.com/orgs/$org/teams?access_token=$accessToken",org)

                model.teams.observe(this, Observer<Array<Team>> { teams ->
                    prefAccess.putPrefLogin(userSuggestions, login)
                    prefAccess.putPrefOrg(orgSuggestions, org)
                    prefAccess.putLogin(login)
                    prefAccess.putOrg(org)
                    prefAccess.putToken(accessToken)

                    val userDetails = Intent(this, UserDetailsActivity::class.java)
                    userDetails.putExtra("profile", profile)
                               .putExtra("org", org)

                    startActivity(userDetails)
                    finish()
                })
                logon = false
            }
            else {
                logon = false
                Toast.makeText(this, getString(R.string.LoginFail),Toast.LENGTH_LONG).show()
            }
        })

        loginButton.setOnClickListener{
            if(!logon){
                logon = true
                logon =
                        if(editUser.text.isEmpty() || editOrg.text.isEmpty()){
                            Toast.makeText(this, R.string.empty_login_fields,Toast.LENGTH_LONG).show()
                            false
                        } else {
                            val accessToken = editAccessToken.text.toString()
                            val urlLogin = "https://api.github.com/user?access_token=$accessToken"
                            model.attemptLogin(urlLogin)
                            false
                        }
            }else{
                Toast.makeText(this, "Loading...",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
