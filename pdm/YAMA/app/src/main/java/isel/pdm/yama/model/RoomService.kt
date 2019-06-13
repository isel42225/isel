package isel.pdm.yama.model

import android.util.Log
import isel.pdm.yama.MembersOfTeam
import isel.pdm.yama.Profile
import isel.pdm.yama.Team
import isel.pdm.yama.net.ProfileDto
import isel.pdm.yama.net.TeamDto
import isel.pdm.yama.utils.AsyncWork
import isel.pdm.yama.utils.runAsync

class RoomService(db : YamaDB){

    companion object {
        fun parseTeam(t : TeamDto, idOrg: String ) = Team(t.name,t.id, idOrg)

        fun parseProfile(p : ProfileDto) = Profile(p.login,p.name,p.email,p.followers,p.avatar_url)
    }

    private val teamsDao = db.teamsDao()
    private val membersDao = db.membersDao()
    private val profileDao = db.profileDao()

    fun findTeamsByOrg(idOrg: String): Array<Team> = teamsDao.findTeamsByOrg(idOrg)

    fun findByIdTeam(idTeam: Int): Array<Profile> = profileDao.findByIdTeam(idTeam)

    fun findProfileById(login: String) = profileDao.findById(login)


    fun saveProfileToDb(it: Array<ProfileDto>): AsyncWork<Array<Profile>> {
        return runAsync {
            if (it.isNotEmpty()) syncSaveProfileFromDto(it)
            else arrayOf()
        }
    }

    fun saveTeamsToDb(teams: Array<TeamDto>, idOrg: String): AsyncWork<Array<Team>>{
        return runAsync {
            if (teams.isNotEmpty()) syncSaveTeamFromDto(teams, idOrg)
            else arrayOf()
        }
    }

    fun saveProfileToMembers(profiles : Array<ProfileDto>, team : Int) : AsyncWork<Array<Profile>>{
        return runAsync {
            if(profiles.isNotEmpty()){
                syncSaveMembers(profiles, team)
            }
            else arrayOf()
        }
    }

    fun syncSaveMembers(profiles : Array<ProfileDto>, team : Int) : Array<Profile>{
        val members = ArrayList<MembersOfTeam>()
        profiles.forEach {
            members.add(MembersOfTeam(it.login, team))
        }
        membersDao.insertMembers(*members.toTypedArray())
        return syncSaveProfileFromDto(profiles)
    }

    fun syncSaveTeamFromDto(it: Array<TeamDto>, idOrg: String): Array<Team> {
        val result = it.map { t-> parseTeam(t, idOrg)}.toTypedArray()
        teamsDao.insertTeams(*result)
        return result
    }


    fun syncSaveProfileFromDto(it: Array<ProfileDto>): Array<Profile> {
        val result = it.map (::parseProfile)    // <=> {dto -> parseProfile(dto)}
                    .toTypedArray()
        profileDao.insertAllProfiles(*result)
        return result
    }
}

