package isel.daw.g08.app.comment

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.HalResponse
import isel.daw.g08.app.Link
import org.joda.time.DateTime
import java.time.Instant
import java.util.*

data class  RequestComment @JsonCreator constructor(val text : String)

fun RequestComment.toCreationComment(issue: Int) = CreationComment(
        issue = issue,
        text = this.text
)

data class CreationComment (
        val issue : Int,
        val text : String,
        val createdOn : DateTime = DateTime.now(),
        val lastUpdatedOn: DateTime? = null
)

fun CreationComment.toComment(id : Int) = Comment(
        id = id,
        issue = this.issue,
        text = this.text,
        createdOn = this.createdOn.toDateResp(),
        lastUpdatedOn = this.lastUpdatedOn)

private fun DateTime.toDateResp() = this.toLocalDate().toString()

data class Comment @JsonCreator constructor(var id : Int? = null,
                                            val issue : Int,
                                            val text : String,
                                            val createdOn : String,
                                            val lastUpdatedOn : DateTime? = null)

