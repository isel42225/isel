package isel.daw.g08.app.dal

import isel.daw.g08.app.issue.CreationIssue
import isel.daw.g08.app.issue.Issue
import isel.daw.g08.app.issue.Label
import isel.daw.g08.app.issue.State
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import org.postgresql.util.PSQLException
import java.lang.IllegalStateException

class IssueMapper {

    fun create(issue : CreationIssue) : Int{
         val id = transaction {
            IssueTable.insert{
                it[name] = issue.name
                it[description] = issue.description
                it[creationDate] = issue.creationDate
                it[closedDate] = issue.closedDate
                it[state] = issue.state.name
                it[projectName] = issue.project
            } get IssueTable.id
        }
        if(id == null)throw IllegalStateException("Error creating issue")
        insertLabels(id, issue.labels)
        return id
    }

    private fun insertLabels(issue : Int, labels : List<Label>) {
        transaction {
                IssueLabelTable.batchInsert(labels) { label ->
                    this[IssueLabelTable.idi] = issue
                    this[IssueLabelTable.idl] = label.name
                }
        }
    }

    fun get(id : Int) : Issue? {
        val row = transaction { IssueTable.select {IssueTable.id eq id}.firstOrNull() }
        if(row == null) return null
        val labels = getLabels(row[IssueTable.id])
        val issue = mapResultRow(row, labels)
        return issue
    }

    fun getAll(project: String) : List<Issue> {
        return transaction {
            IssueTable
                    .select{IssueTable.projectName eq project}
                    .map {
                        val labels = getLabels(it[IssueTable.id])
                        mapResultRow(it, labels)
                    }
        }
    }

    fun getRecent() : List<Issue> {
        return transaction {
            IssueTable
                    .selectAll()
                    .orderBy(IssueTable.creationDate)
                    .map { r -> mapResultRow(r, getLabels(r[IssueTable.id])) }
        }
    }

    fun getByState(state : String) : List<Issue> {
        val issues =  transaction {
                        IssueTable
                            .select{ IssueTable.state eq state}
                            .map { row -> mapResultRow(row, getLabels(row[IssueTable.id])) }
        }
        return issues
    }

    fun getByLabel(label : String) : List<Issue> {
        val ids = transaction {
                                IssueLabelTable
                                        .select { IssueLabelTable.idl eq label }
                                        .map { row -> row[IssueLabelTable.idi]  }
        }

        val issues = ArrayList<Issue>()
        for(id in ids){
            transaction {
                IssueTable
                        .select { IssueTable.id eq id }
                        .map { r -> mapResultRow(r, getLabels(r[IssueTable.id]))}
                        .forEach { issues.add(it) }
            }
        }

        return issues
    }
    private fun getLabels(issue : Int) : List<Label> {
        val labels = ArrayList<Label>()
        transaction {
            (IssueLabelTable innerJoin LabelTable)
                    .slice(LabelTable.name)
                    .select { IssueLabelTable.idi eq issue }
                    .map { Label.valueOf(it[LabelTable.name]) }
                    .forEach{labels.add(it)}
        }
        return labels
    }

    private fun mapResultRow(row : ResultRow, labels : List<Label>) : Issue =
        Issue(
                row[IssueTable.id],
                row[IssueTable.projectName],
                row[IssueTable.name],
                row[IssueTable.description],
                row[IssueTable.creationDate].toDateResp(),
                row[IssueTable.closedDate],
                State.valueOf(row[IssueTable.state]),
                labels)

    private fun DateTime.toDateResp() = this.toLocalDate().toString()

    fun update(issue : Issue) : Int? {
        val id = transaction {
            try {
                IssueTable.update({ IssueTable.id eq issue.id }) {
                    it[name] = issue.name
                    it[description] = issue.description
                    it[creationDate] = DateTime.parse(issue.creationDate)
                    it[closedDate] = issue.closedDate
                    it[state] = issue.state.name
                    it[projectName] = issue.project
                }
            }catch (e : PSQLException){
                null
            }
        }
        updateLabels( issue.id, issue.labels)
        return id
    }

    private fun updateLabels(issue : Int, labels : List<Label>){
        transaction {
            for(i in 0 until labels.size) {
                IssueLabelTable.insert {
                    it[idi] = issue
                    it[idl] = labels[i].name
                }
            }
        }
    }

    fun deleteOf(project : String){
        transaction {
            IssueTable.deleteWhere { IssueTable.projectName eq project}
        }
    }

}