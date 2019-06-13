package isel.daw.g08.app.project

import isel.daw.g08.app.Result

interface IProjectStore{
    fun create(new : Project, user : String) : Result
    fun get(name : String) : Project?
    fun getAll(token : String) : List<Project>
    fun update(value : Project) : Project?
    fun delete (name : String) : String?
}