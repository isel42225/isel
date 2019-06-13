package isel.pdm.yama.viewmodel

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import isel.pdm.yama.Team
import isel.pdm.yama.YamaApplication

class TeamsViewModel(private val app : YamaApplication) : AndroidViewModel(app) {
    val TAG  = "TeamsViewModel"
    val teams : MutableLiveData<Array<Team>> = MutableLiveData()

    fun getTeamsFromDB(org : String){
        app.repo.getTeamsFromDB({
            Log.v(TAG, "fetched teams from Room DB")
            teams.value = it
        },org)
    }
}
