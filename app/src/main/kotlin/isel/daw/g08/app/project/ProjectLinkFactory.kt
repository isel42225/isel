package isel.daw.g08.app.project

import isel.daw.g08.app.Link

/**
 * Factory functions to create the Links for the Project Response
 */
private const val SELF = "self"
private const val SELF_TITLE = "Reload"
private const val PROJECTS_TITLE = "Your projects"
private const val ISSUES = "http://api/projects/issues"
private const val ISSUES_TITLE = "Project Issues"

object ProjectLinkFactory {
    fun createProjectLinks(project : Project) =
            mapOf(projectSelf(project), projectIssues(project))

    fun createProjectsLinks() = mapOf(projectsSelf())

    fun createProjectItemLinks(item: ProjectItemRepr) =
            mapOf(projectItemSelf(item.name))
}


private fun projectSelf(project : Project) =
        SELF to Link("/project/${project.name}", SELF_TITLE)

private fun projectIssues(project : Project)=
        ISSUES to Link("project/${project.name}/issues", ISSUES_TITLE)

private fun projectItemSelf(name : String) = SELF to Link("project/$name", SELF_TITLE)

private fun projectsSelf() = SELF to Link("project", SELF_TITLE)