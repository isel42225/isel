package isel.daw.g08.app.user

interface IUserStore{
    fun create(new : ReqUser) : UserRepr
    fun get(req : ReqUser) : UserRepr
    fun isValid(token : String) : Boolean
    fun hasAccess(token : String, project : String) : Boolean
}