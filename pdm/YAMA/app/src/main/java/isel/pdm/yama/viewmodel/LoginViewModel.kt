package isel.pdm.yama.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import isel.pdm.yama.Profile
import isel.pdm.yama.Team
import isel.pdm.yama.YamaApplication
class LoginViewModel(private val app : YamaApplication) : AndroidViewModel(app){

    val profile : MutableLiveData<Profile> = MutableLiveData()
    val teams: MutableLiveData<Array<Team>> = MutableLiveData()

    fun attemptLogin(url : String) {
        app.repo.getUserProfile(url, {profile.value = it})
    }
    fun verifyOrg(url:String, idOrg: String){
        app.repo.getTeamsOfOrg({teams.value = it}, idOrg, url)
    }

    fun getLoggedUser(login: String){
        app.repo.getLoggedProfile({profile.value = it}, login)
    }
}