package isel.daw.g08.app.dal

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object UserTable: Table(){
    val user  = text("Username") // an hash value of the real value
    val password = text("Password") // an hash value of the real value
    val token = text("Token").primaryKey() // an hash value of the combination of the previous hashes
}

object UserProjectTable: Table(){
    val user = reference("UserToken", UserTable.token).primaryKey()
    val projectName = reference("ProjectName", ProjectTable.name).primaryKey()
}

object ProjectTable: Table(){
    val name = text("Name").primaryKey()
    val description = text("Description")
}

object StateTable: Table(){
    val name = text("Name").primaryKey()
}

object LabelTable: Table(){
    val name = text("Name").primaryKey()
}

object ProjectLabelTable: Table(){
    val label = reference("LabelId", LabelTable.name).primaryKey()
    val project = reference("ProjectName", ProjectTable.name).primaryKey()

}

object ProjectStateTable: Table(){
    val project = reference("ProjectName", ProjectTable.name).primaryKey()
    val state = reference("StateId", StateTable.name).primaryKey()
    val transitions = varchar("transitions", 100)
}

object IssueLabelTable: Table(){
    val idl = reference("LabelId", LabelTable.name).primaryKey()
    val idi = reference("IssueId", IssueTable.id).primaryKey()
}

object IssueTable: Table(){
    val id = integer("Id").autoIncrement().primaryKey()
    val name = text("Name")
    val description = text("Description")
    val creationDate = datetime("CreationDate")
    val closedDate = datetime("ClosedDate").nullable()
    val projectName = reference("ProjectName", ProjectTable.name)
    val state = reference("State", StateTable.name)
}

object CommentTable: Table(){
    val id = integer("Id").autoIncrement().primaryKey()
    val text = text("Text")
    val issue = reference("IssueId", IssueTable.id)
    val createdOn = datetime("CreatedOn")
    val lastUpdatedOn = datetime("LastUpdatedOn").nullable()
}