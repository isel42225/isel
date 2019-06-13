package isel.daw.g08.app.comment

import isel.daw.g08.app.AlreadyExists
import isel.daw.g08.app.Created
import isel.daw.g08.app.Result
import isel.daw.g08.app.dal.CommentMapper
import org.springframework.stereotype.Service

@Service
class CommentStore : ICommentStore {
    private val mapper = CommentMapper()

    override fun create(new: CreationComment) : Result {
        val id = mapper.create(new)
        if(id == null) return AlreadyExists()
        return Created(id.toString())
    }

    override fun get(id: Int): Comment {
        return mapper.get(id)
    }

    override fun getOf(issue: Int): List<Comment> {
        return mapper.getOf(issue)
    }

    override fun update(new: Comment) : Int? {
        return mapper.update(new)
    }

   override fun delete(id : Int) : Int? {
       return mapper.delete(id)
    }

}