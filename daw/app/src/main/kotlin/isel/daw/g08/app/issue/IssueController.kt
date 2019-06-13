package isel.daw.g08.app.issue

import io.swagger.annotations.Api
import isel.daw.g08.app.*
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.daw.g08.app.auth.AccessRequired
import isel.daw.g08.app.comment.CommentsResponse
import isel.daw.g08.app.comment.ICommentStore
import isel.daw.g08.app.project.IProjectStore
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val RESOURCE = "issue"

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping(produces = ["application/hal+json","application/json", "application/vnd.collection+json"])
@Api(value = "Issue Controller", description = "Issue operations")
class IssueController(private val issueStore : IIssueStore,
                             private val commentStore: ICommentStore,
                             private val projectStore : IProjectStore){

    @AccessRequired
    @ApiOperation(value = "Creates a new issue")
    @ApiParam(value = "Project", required = true)
    @PostMapping(Uri.issues)
    fun create(@PathVariable project : String , @RequestBody req : RequestIssue) : ResponseEntity<CreatedIssueResponse>{
        val new = req.toCreationIssue(project)
        val res = issueStore.create(new)
        when (res) {
            is Created ->  return ResponseEntity
                                    .created(Uri.forIssueById(project, res.id))
                                    .body(CreatedIssueResponse(new.toIssue(Integer.valueOf(res.id))))
            is AlreadyExists -> throw AlreadyExistsException()
        }
    }

    @AccessRequired
    @ApiOperation(value = "Find issue by id")
    @GetMapping(Uri.issueById)
    fun get(@PathVariable project : String,
            @PathVariable id : Int) : ResponseEntity<IssueResponse>{

        val issue = issueStore.get(id)
        if(issue == null) throw ResourceNotFoundException(RESOURCE, id.toString())
        val comments = commentStore.getOf(id)
        val commentsResponse = CommentsResponse(comments, id, project)
        return ResponseEntity.ok(IssueResponse(issue, mapOf("Comments" to commentsResponse)))
    }

    @AccessRequired
    @ApiOperation(value = "Get all the issues of a project")
    @GetMapping(Uri.issues)
    fun getAll(@PathVariable project : String) : ResponseEntity<IssuesResponse> {
        val issues = issueStore.getAll(project).map{it.toItemRepr()}.map{IssueItemResponse(it)}
        return ResponseEntity.ok(IssuesResponse(issues, project))
    }

    @AccessRequired
    @ApiOperation(value = "Find the most recent issues project")
    @GetMapping(Uri.recentIssues)
    fun getRecent(@PathVariable project : String) : ResponseEntity<IssuesResponse>{
        val issues = issueStore.getRecent().map{it.toItemRepr()}.map{IssueItemResponse(it)}
        val resp = IssuesResponse(issues, project)
        return ResponseEntity
                .ok(resp)
    }

    @AccessRequired
    @ApiOperation(value = "Find issue by state")
    @GetMapping(Uri.issuesByState)
    fun getByState(@PathVariable project : String ,@PathVariable state : String) : ResponseEntity<IssuesResponse>{
        val issues = issueStore.getByState(state).map{it.toItemRepr()}.map{IssueItemResponse(it)}
        val resp = IssuesResponse(issues, project)
        return ResponseEntity
                .ok(resp)
    }

    @AccessRequired
    @ApiOperation(value = "Find issue by label")
    @GetMapping(Uri.issuesByLabel)
    fun getByLabel(@PathVariable project : String , @PathVariable label : String) :ResponseEntity<IssuesResponse> {
        val issues = issueStore.getByLabel(label).map{it.toItemRepr()}.map{IssueItemResponse(it)}
        val resp = IssuesResponse(issues,project)
        return ResponseEntity
                .ok(resp)
    }

    @AccessRequired
    @ApiOperation(value = "Change the state of an issue")
    @PutMapping(Uri.changeIssueState)
    fun changeState(@PathVariable project : String,
                    @PathVariable issue : Int,
                    @PathVariable state : String) : ResponseEntity<Unit> {
        // Ver os states possiveis de transitar
        // Comparar com o enviado
        // Mudar e responder
        val oldIssue = issueStore.get(issue) ?: throw ResourceNotFoundException(RESOURCE, issue.toString())
        val oldState = oldIssue.state
        val p = projectStore.get(project)?: throw ResourceNotFoundException(RESOURCE, issue.toString())
        val transitions = p.transitions.getOrDefault(oldState, listOf())
        val newState = State.valueOf(state)

        if(!transitions.contains(newState)) {
            throw InvalidIssueStateTransition(oldState.name, newState.name, transitions)
        }
        oldIssue.state = newState
        issueStore.update(oldIssue)
        return ResponseEntity
                .ok()
                .build()
    }
}