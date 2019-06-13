package isel.daw.g08.app.home

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.models.HttpMethod
import isel.daw.g08.app.Uri
import org.springframework.http.MediaType

private const val PROJECT_REL = "http://api/projects"
private const val USER_REL = "http://api/user"

private const val ALLOW_HINT = "allow"
private const val ACCEPT_PUT_HINT = "accept-put"


typealias Hints = Map<String,Hint>
data class Hint(val value : List<String>)
data class Resource (val href : String, val hints : Hints)

data class HomeResponse @JsonCreator constructor(val resources : Map<String,Resource>){
    companion object {
        fun build() : HomeResponse{
            val resources = buildResources()
            return HomeResponse(resources)
        }
    }
}
 private fun buildResources () : Map<String, Resource> {
     return mapOf(
           PROJECT_REL to Resource(Uri.project, buildProjectHints()),
           USER_REL to Resource(Uri.user, buildUserHints())
     )
 }

private fun buildUserHints() =
        mapOf(
                ALLOW_HINT to Hint(listOf(HttpMethod.GET.name, HttpMethod.PUT.name)),
                ACCEPT_PUT_HINT to Hint(listOf(MediaType.APPLICATION_JSON.toString()))
        )

private fun buildProjectHints() =
        mapOf(
                ALLOW_HINT to Hint(listOf(HttpMethod.GET.name, HttpMethod.PUT.name)),
                ACCEPT_PUT_HINT to Hint(listOf(MediaType.APPLICATION_JSON.toString()))
        )