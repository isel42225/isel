package isel.daw.g08.app.comment


import io.swagger.annotations.Api
import isel.daw.g08.app.*
import io.swagger.annotations.ApiOperation
import isel.daw.g08.app.auth.AccessRequired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriTemplate

private const val RESOURCE = "comment"

@RestController
@RequestMapping(produces = ["application/json", "application/hal+json"])
@Api(value = "Comment Controller", description = "Comment operations")
class CommentController(private val store : ICommentStore){

    @AccessRequired
    @ApiOperation(value = "Create a comment")
    @PostMapping(Uri.comments)
    fun createComment(@PathVariable project : String, @PathVariable issue : String, @RequestBody req : RequestComment) : ResponseEntity<CommentResponse> {

        val new = req.toCreationComment(issue.toInt())
        val res = store.create(new)
        when(res){
            is Created -> {
                return ResponseEntity
                    .created(Uri.forCommentsById(project, issue, res.id))
                    .body(CommentResponse(new.toComment(res.id.toInt()), project))}
            is AlreadyExists -> throw AlreadyExistsException()
        }
    }

    @AccessRequired
    @ApiOperation(value = "Return all comments")
    @GetMapping(Uri.comments)
    fun getComments(@PathVariable project : String, @PathVariable issue : Int): ResponseEntity<CommentsResponse>{
        val comments = store.getOf(issue)
        return ResponseEntity
                .ok(CommentsResponse(comments, issue, project))
    }


    @AccessRequired
    @ApiOperation(value = "Find and update a comment")
    @PutMapping(Uri.commentsById)
    fun editComment(@PathVariable project : String , @PathVariable issue : String, @RequestBody new : Comment)
            : ResponseEntity<CommentResponse>{
        val id = store.update(new)
        if(id == null) throw ResourceNotFoundException(RESOURCE, new.id.toString())

        return ResponseEntity
                .created(Uri.forCommentsById(project, issue, id.toString()))
                .body(CommentResponse(new, project))

    }

    @AccessRequired
    @ApiOperation(value = "Delete a comment")
    @DeleteMapping(Uri.commentsById)
    fun deleteComment(@PathVariable comment : Int) : ResponseEntity<Unit>{
        if(store.delete(comment) == null) throw ResourceNotFoundException(RESOURCE, comment.toString())
        return ResponseEntity.ok().build()
    }
}