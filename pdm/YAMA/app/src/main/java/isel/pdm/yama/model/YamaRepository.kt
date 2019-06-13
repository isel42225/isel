package isel.pdm.yama.model

import android.graphics.Bitmap
import isel.pdm.yama.Profile
import isel.pdm.yama.Team
import isel.pdm.yama.YamaApplication
import isel.pdm.yama.net.TeamDto
import isel.pdm.yama.utils.runAsync

class YamaRepository(
        private val app: YamaApplication,
        private val api : ApiService,
        private val db: RoomService) {



    fun fetchImage(url: String, success : (Bitmap) -> Unit) {
        runAsync {
            api.getImage(url, success)
        }
    }

    fun getProfilesOfTeam(success: (Array<Profile>) -> Unit, idTeam: Int, url: String){
        runAsync {
            db.findByIdTeam(idTeam)
        }.andThen { it ->
            if(it.isEmpty()){
                api.getMembers(url) { arrayDto ->
                    db.saveProfileToMembers(arrayDto, idTeam).andThen(success)
                    db.saveProfileToDb(arrayDto)
                }
            }
            else {
                success(it)
            }
        }
    }

    fun getTeamsFromDB(success: (Array<Team>) -> Unit, idOrg: String){
        runAsync {
            db.findTeamsByOrg(idOrg)
        }.andThen({it -> success(it)})
    }

    fun getLoggedProfile(success: (Profile) -> Unit, login: String){
        runAsync {
            db.findProfileById(login)
        }.andThen ({it -> success(it)})
    }

    fun getTeamsOfOrg(success: (Array<Team>) -> Unit, idOrg: String, url: String){
        runAsync {
            db.findTeamsByOrg(idOrg)
        }.andThen { it ->
            if(it.isEmpty()){
                api.verifyOrg(url) { arrayDto ->
                    db.saveTeamsToDb(arrayDto, idOrg).andThen(success)
                }
            }
            else {
                success(it)
            }
        }
    }

    fun getUserProfile(url : String, success: (Profile) -> Unit){
        runAsync {
            api.attemptLogin(url,
                {dto -> success(RoomService.parseProfile(dto))}
            )
        }
    }

    fun syncUpdateTeams(token:String, org:String){
        val teams = api.syncFetchTeams(token, org)
        teams.forEach { team -> saveTeamMembersToDb(token, team)}
        db.syncSaveTeamFromDto(teams, org)
    }

    private fun saveTeamMembersToDb(token : String, team : TeamDto){
        val tid = team.id
        val members = api.syncFetchMembers(token, tid)
        db.syncSaveMembers(members, tid)
        db.syncSaveProfileFromDto(members)
    }

}