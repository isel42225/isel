package isel.daw.g08.app.user

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping(path = ["/user"], produces = ["application/hal+json", "application/json"])
@Api(value = "User Controller", description = "User operations")
class UserController (private val store : UserStore){


    @PutMapping
    @ApiOperation(value = "Create a new user")
    fun create(@RequestBody new : ReqUser) : ResponseEntity<UserResponse> {
        val repr = store.create(new)
        return ResponseEntity
                .created(URI.create("/user/${new.name}"))
                .body(UserResponse(repr))
    }

    @PostMapping
    @ApiOperation(value= "Get user token")
    fun get(@RequestBody req : ReqUser) : ResponseEntity<UserResponse> {
        val repr = store.get(req)
        return  ResponseEntity.ok(UserResponse((repr)))
    }



}