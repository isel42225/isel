package isel.pdm.yama.model

import androidx.room.*
import isel.pdm.yama.MembersOfTeam
import isel.pdm.yama.Profile
import isel.pdm.yama.Team

@Dao
interface ProfileDao{
    @Query("SELECT * FROM profile WHERE login = :login")
    fun findById(login: String): Profile

    @Query("SELECT * FROM membersOfTeam  INNER JOIN profile on profile.login = membersOfTeam.loginProfile WHERE idTeam = :idTeam")
    fun findByIdTeam(idTeam: Int) : Array<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProfiles(vararg profiles: Profile)
}

@Dao
interface TeamsDao{
    @Query("SELECT * FROM team WHERE idOrg = :org")
    fun findTeamsByOrg(org: String): Array<Team>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(vararg teams: Team)
}

@Dao
interface MembersDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMembers(vararg members: MembersOfTeam)
}

@Database(entities = [Profile::class, MembersOfTeam::class, Team::class], version = 1)
abstract class YamaDB : RoomDatabase(){
    abstract fun profileDao(): ProfileDao
    abstract fun teamsDao(): TeamsDao
    abstract fun membersDao(): MembersDao
}
