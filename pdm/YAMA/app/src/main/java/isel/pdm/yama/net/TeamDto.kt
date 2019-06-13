package isel.pdm.yama.net

import java.io.Serializable

/**
 * The TeamDto have only the name and the id of the team
 */
data class TeamDto (
    val name : String?,
    val id : Int
) : Serializable
