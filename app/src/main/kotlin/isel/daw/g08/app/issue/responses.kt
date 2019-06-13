package isel.daw.g08.app.issue

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.HalResponse
import isel.daw.g08.app.comment.CommentsResponse

data class CreatedIssueResponse @JsonCreator constructor(val issue: Issue)
    : HalResponse(IssueLinkFactory.createIssueLinks(issue))

data class IssueResponse @JsonCreator constructor(val issue : Issue,
                                                  val _embedded : Map<String, CommentsResponse>)
    : HalResponse(IssueLinkFactory.createIssueLinks(issue))

data class IssueItemResponse @JsonCreator constructor(val issue : IssueItemRepr) : HalResponse(IssueLinkFactory.createIssueItemLinks(issue))

data class IssuesResponse @JsonCreator constructor(val issues : List<IssueItemResponse>,
                                                   val project : String)
    : HalResponse(IssueLinkFactory.createIssuesLinks(project))