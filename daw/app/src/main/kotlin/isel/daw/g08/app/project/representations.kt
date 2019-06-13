package isel.daw.g08.app.project

data class ProjectItemRepr (val name : String, val description: String)

fun Project.toItemRepr() = ProjectItemRepr(this.name, this.description)