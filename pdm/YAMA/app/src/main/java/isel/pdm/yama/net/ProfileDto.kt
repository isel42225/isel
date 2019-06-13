package isel.pdm.yama.net

/**
 * The ProfileDto have a information of a person in Github
 */
data class ProfileDto(
    val login : String,
    val name : String?,
    val email : String?,
    val followers : Int,
    val avatar_url : String
)


