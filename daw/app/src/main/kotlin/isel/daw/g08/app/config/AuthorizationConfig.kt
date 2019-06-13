package isel.daw.g08.app.config

import isel.daw.g08.app.ProblemJson
import isel.daw.g08.app.auth.AccessInterceptor
import isel.daw.g08.app.auth.AuthorizationException
import isel.daw.g08.app.auth.AuthorizationInterceptor
import isel.daw.g08.app.auth.MissingAuthHeaderException
import isel.daw.g08.app.user.IUserStore
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ControllerAdvice
@EnableWebMvc
open class AuthorizationConfig(val store : IUserStore) : WebMvcConfigurer {

    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthException(e : AuthorizationException) : ResponseEntity<ProblemJson> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PROBLEM_JSON
        return ResponseEntity
                .status(e.status())
                .headers(headers)
                .body(ProblemJson.of(e))
    }

    private fun AuthorizationException.status() =
            when(this) {
                is MissingAuthHeaderException -> HttpStatus.UNAUTHORIZED
                else -> HttpStatus.FORBIDDEN
            }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthorizationInterceptor(store))
        registry.addInterceptor(AccessInterceptor(store))
    }
}