package isel.daw.g08.app.issue

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import org.joda.time.DateTime

data class RequestIssue @JsonCreator constructor(
        val name : String,
        val description: String,
        val labels : List<Label> = emptyList()
)

fun RequestIssue.toCreationIssue(project : String) = CreationIssue (
        project = project,
        name = name,
        description = description,
        labels = labels
)

data class CreationIssue(
        val project : String,
        val name : String,
        var description : String,
        val creationDate : DateTime = DateTime.now(),
        var closedDate : DateTime?  = null,
        var state : State = State.OPEN,
        val labels : List<Label> = emptyList()
)

fun CreationIssue.toIssue(id : Int ) = Issue(
        id,
        this.project,
        this.name,
        this.description,
        this.creationDate.toDateResp(),
        this.closedDate,
        this.state,
        this.labels
)

private fun DateTime.toDateResp() = this.toLocalDate().toString()


/**
 * Model class Issue
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Issue @JsonCreator constructor(
        val id : Int,
        val project : String,
        val name : String,
        var description : String,
        val creationDate : String,
        var closedDate : DateTime?  = null,
        var state : State = State.OPEN,
        val labels : List<Label> = emptyList())

