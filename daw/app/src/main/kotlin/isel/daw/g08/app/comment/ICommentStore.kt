package isel.daw.g08.app.comment

import isel.daw.g08.app.Result

interface ICommentStore {
    fun create(new : CreationComment) : Result
    fun get (id : Int) : Comment
    fun getOf(issue : Int) : List<Comment>
    fun update(new : Comment) : Int?
    fun delete(id : Int) : Int?

}