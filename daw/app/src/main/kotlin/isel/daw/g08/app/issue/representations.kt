package isel.daw.g08.app.issue

data class IssueItemRepr(val id : Int, val project : String, val name : String, val createdOn : String, val state : State)

fun Issue.toItemRepr () = IssueItemRepr(this.id, this.project, this.name, this.creationDate, this.state)