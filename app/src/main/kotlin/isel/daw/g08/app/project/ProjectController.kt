package isel.daw.g08.app.project

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.daw.g08.app.*
import isel.daw.g08.app.auth.AccessRequired
import isel.daw.g08.app.auth.AuthenticationRequired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val RESOURCE = "project"

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping(produces = ["application/json", "application/hal+json"])
@Api(value = "Project Controller", description = "Project operations")
class ProjectController (private val projectStore : IProjectStore){

    @AuthenticationRequired
    @ApiOperation(value = "Creates a new project")
    @PutMapping(Uri.project)
    fun createProject(@RequestHeader("Authorization")auth :String, @RequestBody value : Project) : ResponseEntity<ProjectResponse>{
        val token = auth.split(" ").last()
        val result = projectStore.create(value, token)
        when(result){
            is Created  -> return ResponseEntity
                                    .created(Uri.forProjectByName(result.id))
                                    .body(ProjectResponse(value))
            is AlreadyExists -> throw AlreadyExistsException()
        }
    }

    @AccessRequired
    @ApiOperation(value = "Find project by name")
    @ApiParam(value = "Project name", required = true)
    @GetMapping(Uri.projectByName)
    fun getProject(@PathVariable name : String) : ResponseEntity<ProjectResponse>{
        val project = projectStore.get(name)
        if(project == null) throw ResourceNotFoundException(RESOURCE, name)
        return ResponseEntity.ok(ProjectResponse(project))
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @AccessRequired
    @ApiOperation(value = "Find all the projects of a user")
    @GetMapping(Uri.project)
    fun getAllProjects(@RequestHeader(value = "Authorization") authHeader : String) : ResponseEntity<ProjectsResponse> {
        val token = authHeader.split(" ").last()
        val projects = projectStore.getAll(token).map { p -> p.toItemRepr() }.map { repr -> ProjectItemResponse(repr) }
        return ResponseEntity.ok(ProjectsResponse(projects))
    }

    @AccessRequired
    @ApiOperation(value = "Find and update project by name")
    @PutMapping(Uri.projectByName)
    fun updateProject( @RequestBody value : Project): ResponseEntity<ProjectResponse> {
        val result  = projectStore.update(value)
        if(result == null)throw ResourceNotFoundException(RESOURCE, value.name)
        return ResponseEntity.ok(ProjectResponse(value))
    }
}