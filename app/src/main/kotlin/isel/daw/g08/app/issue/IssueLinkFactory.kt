package isel.daw.g08.app.issue

import isel.daw.g08.app.Link
import isel.daw.g08.app.Uri
import isel.daw.g08.app.comment.CommentResponse
import isel.daw.g08.app.comment.CommentsResponse

/**
 * Factory functions to create the Links for the Issue Response
 */

private const val SELF = "self"
private const val SELF_TITLE = "Reload"
private const val RECENT = "http://api/issue/recent"
private const val RECENT_TITLE = "Recent issues"
private const val STATE = "http://api/issue/by-state"
private const val STATE_TITLE = "Issues by state"
private const val LABEL = "http://api/issue/by-label"
private const val LABEL_TITLE = "Issues by label"
private const val PROJECT = "http://api/projects"
private const val PROJECT_TITLE = "Project"
private const val COMMENTS = "http://api/issue/comments"

object IssueLinkFactory {
    fun createIssueLinks(issue : Issue) = mapOf(
            issueSelf(issue),
            issueRecent(issue),
            issueState(issue),
            issueLabel(issue),
            issueProject(issue)
    )

    fun createIssueComments(comments : CommentsResponse) =
            COMMENTS to comments

    fun createIssueItemLinks(issueItemRepr: IssueItemRepr) =
            mapOf(
                issueItemSelf(issueItemRepr)
            )

    fun createIssuesLinks(project : String)=
            mapOf(issuesSelf(project),issuesProject(project))
}


private fun issueSelf(issue : Issue) =
        SELF to Link("/project/${issue.project}/issues/${issue.id}", SELF_TITLE)

private fun issueItemSelf(issueItemRepr: IssueItemRepr) =
        SELF to Link("project/${issueItemRepr.project}/issues/${issueItemRepr.id}", SELF_TITLE)

private fun issueRecent(issue : Issue) =
        RECENT to Link("/project/${issue.project}/recent", RECENT_TITLE)

private fun issueState(issue : Issue) =
        STATE to Link("/project/${issue.project}/issues/{state}", STATE_TITLE)

private fun issueLabel(issue: Issue) =
        LABEL to Link("/project/${issue.project}/issues/{label}", LABEL_TITLE)

private fun issueProject(issue : Issue) =
        PROJECT to Link("/project/${issue.project}", PROJECT_TITLE)



private fun issuesSelf(project : String) =
        SELF to Link("/project/$project/issues", SELF_TITLE)

private fun issuesProject(project : String) =
        PROJECT to Link("/project/$project", PROJECT_TITLE)
