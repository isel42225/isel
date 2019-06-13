package isel.pdm.yama.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import isel.pdm.yama.Profile
import isel.pdm.yama.YamaApplication

class MembersViewModel(private val app: YamaApplication) : AndroidViewModel(app){

    val members : MutableLiveData<Array<Profile>> = MutableLiveData()

    fun getMembers(url : String, idTeam: Int) {
        app.repo.getProfilesOfTeam({members.value = it}, idTeam, url)
    }

}
