package isel.daw.g08.app.repository

import isel.daw.g08.app.dal.*
import isel.daw.g08.app.issue.Label
import isel.daw.g08.app.issue.State
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


class DBConnection {

    var user: String = System.getenv("daw_user")
    var pw: String = System.getenv("daw_pw")

    init {
        Database.connect("jdbc:postgresql://localhost:5432/DAW", driver = "org.postgresql.Driver", user = user, password = pw)
        dropTables()
        createTables()
        initialize()
    }

    private fun createTables() {
        transaction {
            SchemaUtils
                    .create(UserTable,
                            UserProjectTable,
                            ProjectTable,
                            StateTable,
                            LabelTable,
                            ProjectLabelTable,
                            ProjectStateTable,
                            IssueLabelTable,
                            IssueTable,
                            CommentTable)
        }
    }
    private fun dropTables(){
        transaction {
            SchemaUtils
                    .drop(UserTable,
                            UserProjectTable,
                            ProjectTable,
                            StateTable,
                            LabelTable,
                            ProjectLabelTable,
                            ProjectStateTable,
                            IssueLabelTable,
                            IssueTable,
                            CommentTable)
        }
    }
    private fun initialize(){

        transaction {
            val states = State.values()
            for(i in 0 until states.size) {
                StateTable.insert {
                    it[name] = states[i].name
                }
            }

            val labels = Label.values()
            for(i in 0 until labels.size) {
                LabelTable.insert {
                    it[name] = labels[i].name
                }
            }
        }
    }

}

