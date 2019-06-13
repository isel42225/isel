package isel.daw.g08.app.project

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.HalResponse
import isel.daw.g08.app.issue.Label
import isel.daw.g08.app.issue.State
import org.springframework.http.MediaType

data class ProjectResponse @JsonCreator constructor(val project : Project)
    : HalResponse(ProjectLinkFactory.createProjectLinks(project))

data class ProjectItemResponse @JsonCreator constructor(val project : ProjectItemRepr)
    : HalResponse(ProjectLinkFactory.createProjectItemLinks(project))

data class ProjectsResponse @JsonCreator constructor(val projects : List<ProjectItemResponse>,
                                                     val _templates : ProjectListTemplate = ProjectListTemplate.build())
    : HalResponse(ProjectLinkFactory.createProjectsLinks())

data class ProjectListTemplate @JsonCreator constructor(val default : TemplateObject){
    companion object {
        fun build () : ProjectListTemplate {
            val properties = listOf(
                    TemplateProperty(name = "Name"),
                    TemplateProperty(name = "Description"),
                    TemplateProperty(name = "Labels", value = Label.values()),
                    TemplateProperty(name = "States", value = State.values())
            )
            val default = TemplateObject(
                    title = "Create",
                    method= "Put",
                    contentType = MediaType.APPLICATION_JSON.toString(),
                    properties = properties )
            return ProjectListTemplate(default)
        }
    }
}

data class TemplateObject @JsonCreator constructor(val title : String, val method : String, val contentType : String , val properties : List<TemplateProperty>)

data class TemplateProperty @JsonCreator constructor(val name : String, val prompt : String = name, val value : Any = "")