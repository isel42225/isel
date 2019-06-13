package isel.daw.g08.app

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import isel.daw.g08.app.auth.AuthorizationException
import isel.daw.g08.app.auth.ForbiddenAccessException
import isel.daw.g08.app.auth.UserNotFoundException
import isel.daw.g08.app.auth.InvalidPasswordException
import isel.daw.g08.app.issue.State
import org.springframework.http.HttpStatus

private const val AUTH_ERROR_URL = "http://api/error/auth"
private const val NOT_FOUND_URL = "http://api/error/not-found"
private const val CONFLICT_URL = "http://api/error/conflict"
private const val NO_USER_TITLE = "User not found"
private const val INVALID_CREDENTIALS_TITLE = "Your credentials are invalid"
private const val FORBIDDEN_ACCESS_TITLE = "Forbidden access"
private const val DEFAULT_AUTH_TITLE = "Authorization required"
private const val RESOURCE_NOT_FOUND_TITLE = "Resource not found"
private const val CONFLICT_TITLE = "Resource already exists"


/**
 * Type : An URI reference that identifies the problem type
 * Title(optional) : A short summary of the problem type
 * Status : The HTTP status code representative of the problem
 * Detail : An explanation specific to this occurrence of the problem
 * Instance(optional) : A URI reference that identifies the specific
                        occurrence of the problem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson @JsonCreator constructor(
        val type : String,
        val title : String?,
        val status : Int,
        val detail : String,
        val instance : String? = null){

    companion object {

        fun of(e : AuthorizationException) : ProblemJson {
            return when(e){
                is UserNotFoundException -> noUser(e)
                is InvalidPasswordException -> invalidPassword(e)
                is ForbiddenAccessException -> forbiddenAccess(e)
                else -> defaultAuth(e)
            }
        }

        fun resourceNotFound(resource :String, id : String) = ProblemJson(NOT_FOUND_URL,
                                                                          RESOURCE_NOT_FOUND_TITLE,
                                                                          HttpStatus.NOT_FOUND.value(),
                                                                   "$resource with the identifier $id not found")
        fun conflict() =
                ProblemJson(CONFLICT_URL,
                        CONFLICT_TITLE,
                        HttpStatus.CONFLICT.value(),
                        "The resource you are trying to create already exists")

        fun invalidStateTransition(currentState : String, targetState : String ) =
                ProblemJson("http://api/error/invalid-issue-transition",
                        "Invalid issue state transition",
                        HttpStatus.BAD_REQUEST.value(),
                        "The transition from $currentState state to $targetState state is not possible, please check transitions field below for the valid state transitions"
                )

        private fun noUser(e : UserNotFoundException) =
            ProblemJson(AUTH_ERROR_URL,
                        NO_USER_TITLE,
                        HttpStatus.FORBIDDEN.value(),
                        e.msg)

        private fun invalidPassword(e : InvalidPasswordException) =
                ProblemJson(AUTH_ERROR_URL,
                            INVALID_CREDENTIALS_TITLE,
                            HttpStatus.FORBIDDEN.value(),
                            e.msg)

        private fun forbiddenAccess(e : ForbiddenAccessException) =
                ProblemJson(AUTH_ERROR_URL,
                            FORBIDDEN_ACCESS_TITLE,
                            HttpStatus.FORBIDDEN.value(),
                            e.msg)

        private fun defaultAuth(e : AuthorizationException)  =
                ProblemJson(AUTH_ERROR_URL, DEFAULT_AUTH_TITLE, HttpStatus.UNAUTHORIZED.value(), e.msg)
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StateTransitionProblemJson @JsonCreator constructor(
        val type : String,
        val title : String?,
        val status : Int,
        val detail : String,
        val instance : String? = null,
        val validTransitions : List<State>
)



