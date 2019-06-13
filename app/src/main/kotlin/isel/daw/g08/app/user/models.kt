package isel.daw.g08.app.user

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.HalResponse

data class ReqUser @JsonCreator constructor(val name : String,
                                            val password : String)

data class UserRepr (val token : String)

data class UserResponse @JsonCreator constructor(val user : UserRepr) : HalResponse(emptyMap())