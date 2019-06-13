package isel.daw.g08.app.dal

import isel.daw.g08.app.comment.Comment
import isel.daw.g08.app.comment.CreationComment
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException

class CommentMapper {

    fun create(comment : CreationComment) : Int? {
        val id = transaction {
            try {
                CommentTable.insert{
                    it[CommentTable.issue] = comment.issue
                    it[CommentTable.text] = comment.text
                    it[CommentTable.createdOn] = DateTime(comment.createdOn)
                }.get(CommentTable.id)
            }catch (e : PSQLException){
                null
            }
        }
       return id
    }

    fun get(id : Int) : Comment{
        return transaction {
            val row = CommentTable
                        .select{CommentTable.id eq id}.firstOrNull()
            if(row == null) throw Exception()
            mapResultRow(row)

        }
    }

    fun getOf(issue : Int) : List<Comment> {
        return transaction {
            CommentTable
                    .select { CommentTable.issue eq issue}
                    .map { mapResultRow(it) }
        }
    }

    fun update(comment: Comment) : Int? {
        val id = transaction {
            try {
                CommentTable.update {
                    it[CommentTable.text] = comment.text
                    it[CommentTable.lastUpdatedOn] = comment.lastUpdatedOn
                }
            }catch (e : PSQLException){
                null
            }
        }
        return id
    }

    fun delete(comment : Int) : Int? {
        val id = transaction {
            try{
            CommentTable.deleteWhere { CommentTable.id eq comment }
        }catch (e : PSQLException){
                null
            }
        }
        return id
    }

    private fun mapResultRow(row : ResultRow) : Comment {
        return Comment(row[CommentTable.issue],
                       row[CommentTable.id],
                       row[CommentTable.text],
                       row[CommentTable.createdOn].toDateResp())
    }

    private fun DateTime.toDateResp() = this.toLocalDate().toString()
}