package isel.daw.g08.app.comment

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.HalResponse

data class CommentResponse @JsonCreator constructor(val comment : Comment,
                                                    val project : String)
    : HalResponse(CommentLinkFactory.makeCommentLinks(comment, project))


data class CommentsResponse @JsonCreator constructor(val comments : List<Comment>,
                                                     val issue : Int,
                                                     val project : String)
    : HalResponse(CommentLinkFactory.makeCommentsLinks(project, issue))