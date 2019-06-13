package isel.daw.g08.app

import org.springframework.web.util.UriTemplate

object Uri {
    fun forProjectByName(name : String) = projectByNameTemplate.expand(name)
    fun forIssueById(project : String, id : String) = issuesByIdTemplate.expand(project, id)
    fun forCommentsById(project: String, issue : String, id : String) = commentsByIdTemplate.expand(project, issue, id)

    const val user = "/user"
    const val project = "/project"
    const val projectByName = "/project/{name}"
    private val projectByNameTemplate = UriTemplate(projectByName)

    const val issues = "/project/{project}/issues"
    const val recentIssues = "/project/{project}/issues/recent"
    const val issueById = "/project/{project}/issues/{id}"
    const val issuesByState = "/project/{project}/issues/by-state/{state}"
    const val issuesByLabel = "/project/{project}/issues/by-label/{label}"
    const val changeIssueState = "/project/{project}/issues/{issue}/{state}"
    private val issuesByIdTemplate = UriTemplate(issueById)


    const val comments = "/project/{project}/issues/{issue}/comments"
    const val commentsById = "/project/{project}/issues/{issue}/comments/{id}"
    private val commentsByIdTemplate = UriTemplate(commentsById)

}