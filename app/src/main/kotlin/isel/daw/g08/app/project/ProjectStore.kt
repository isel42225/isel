package isel.daw.g08.app.project

import isel.daw.g08.app.*
import isel.daw.g08.app.AlreadyExistsException
import isel.daw.g08.app.dal.ProjectMapper
import org.springframework.stereotype.Service

@Service
class ProjectStore() : IProjectStore {

    val mapper = ProjectMapper()

    override fun create(new: Project, user: String) : Result{
        try{
            mapper.create(new, user)
            return Created(new.name)
        }catch (e : AlreadyExistsException){
            return AlreadyExists()
        }
    }

    override fun update(value: Project) : Project? {
        return mapper.update(value)
    }

    override fun get(name: String) : Project?  {
            return mapper.get(name)
    }

    override fun getAll(token: String): List<Project> {
        return mapper.getAll(token)
    }

    override fun delete(name: String) : String? {
        return mapper.delete(name)
    }



}