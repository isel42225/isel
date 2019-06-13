package isel.daw.g08.app.config

import isel.daw.g08.app.*
import isel.daw.g08.app.issue.State
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ControllerAdvice
@EnableWebMvc
class ErrorConfig : WebMvcConfigurer {

    @ExceptionHandler(AlreadyExistsException::class)
    fun handleConflict(e : AlreadyExistsException) : ResponseEntity<ProblemJson> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON
        return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).body(ProblemJson.conflict())
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(e : ResourceNotFoundException) : ResponseEntity<ProblemJson> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProblemJson.resourceNotFound(e.resource, e.id))
    }

    @ExceptionHandler(InvalidIssueStateTransition::class)
    fun handleInvalidIssueStateTransition(e : InvalidIssueStateTransition) : ResponseEntity<StateTransitionProblemJson> {
        val error = ProblemJson
                .invalidStateTransition(e.currentState, e.targetState)
                .toStateTransitionProblem(e.validStates)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error)
    }
}

fun ProblemJson.toStateTransitionProblem(states : List<State>) =
        StateTransitionProblemJson(
                type = type,
                title = title,
                status = status,
                detail = detail,
                instance = instance,
                validTransitions = states)

