package isel.daw.g08.app.issue

import isel.daw.g08.app.Result


interface IIssueStore{
    fun create(new : CreationIssue) : Result
    fun get(id : Int) : Issue?
    fun getAll(project : String) : List<Issue>
    fun getRecent() : List<Issue>
    fun getByState(state : String) : List<Issue>
    fun getByLabel(label : String) : List<Issue>
    fun update(new : Issue) : Int?
}