package isel.daw.g08.app.auth

private const val NO_HEADER_MSG = "You must provide authorization header"
private const val NO_USER_MSG = "There is no user with the name {user}"
private const val PASSWORD_MISMATCH_MSG = "Wrong password, check if you have CAPS LOCK active"
private const val FORBIDDEN_ACCESS_MSG = "You do not have access to this resource"


abstract class AuthorizationException(val msg : String) : Exception(msg)
class MissingAuthHeaderException : AuthorizationException(NO_HEADER_MSG)
class UserNotFoundException(user : String)
    : AuthorizationException(NO_USER_MSG.replace("{user}",user))
class InvalidPasswordException : AuthorizationException(PASSWORD_MISMATCH_MSG)
class ForbiddenAccessException : AuthorizationException(FORBIDDEN_ACCESS_MSG)
