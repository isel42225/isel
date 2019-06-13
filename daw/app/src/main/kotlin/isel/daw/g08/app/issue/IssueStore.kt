package isel.daw.g08.app.issue

import isel.daw.g08.app.AlreadyExists
import isel.daw.g08.app.Created
import isel.daw.g08.app.Result
import isel.daw.g08.app.AlreadyExistsException
import isel.daw.g08.app.dal.IssueMapper
import org.springframework.stereotype.Service

@Service
class IssueStore : IIssueStore {
    private val mapper = IssueMapper()

    override fun create(new: CreationIssue) : Result {
        try{
            val id = mapper.create(new)
            return Created(id.toString())
        }catch (e : AlreadyExistsException){
            return AlreadyExists()
        }
    }

    override fun get(id: Int): Issue? {
        return mapper.get(id)
    }

    override fun getAll(project: String): List<Issue> {
        return mapper.getAll(project)
    }

    override fun getByState(state: String): List<Issue> {
        return mapper.getByState(state)
    }

    override fun getByLabel(label: String): List<Issue> {
        return mapper.getByLabel(label)
    }

    override fun getRecent(): List<Issue> {
        return mapper.getRecent()
    }

    override fun update(new: Issue) : Int? {
        return mapper.update(new)
    }

}