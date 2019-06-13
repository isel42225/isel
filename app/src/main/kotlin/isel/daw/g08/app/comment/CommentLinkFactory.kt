package isel.daw.g08.app.comment

import isel.daw.g08.app.Link

private const val SELF = "self"
private const val SELF_TITLE = "Reload"
private const val ISSUE = "http://api/issue"
private const val ISSUE_TITLE = "Issue"

object CommentLinkFactory {

    fun makeCommentLinks(comment: Comment, project: String) = mapOf(
            commentSelf(comment, project),
            commentIssue(comment, project)
    )

    fun makeCommentsLinks(project: String, issue: Int) = mapOf(
            commentsSelf(project, issue),
            commentsIssue(project, issue)
    )
}

    private fun commentSelf(comment: Comment, project: String) =
            SELF to Link("/project/$project/issue/${comment.issue}/comments/${comment.id}", SELF_TITLE)

    private fun commentIssue(comment : Comment, project : String) =
            ISSUE to Link("/project/$project/issue/${comment.issue}", ISSUE_TITLE)

    private fun commentsSelf(project:String, issue: Int) =
            SELF to Link("/project/$project/issues/$issue/comments", SELF_TITLE)

    private fun commentsIssue(project: String, issue: Int) =
            ISSUE to Link("project/$project/issues/$issue", ISSUE_TITLE)