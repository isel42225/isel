package isel.daw.g08.app.dal

import isel.daw.g08.app.AlreadyExistsException
import isel.daw.g08.app.issue.Label
import isel.daw.g08.app.issue.State
import isel.daw.g08.app.project.Project
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException


class ProjectMapper{

    fun create(project : Project, token : String) : String {
        transaction {
            try {
                ProjectTable.insert {
                    it[name] = project.name
                    it[description] = project.description
                }
                ProjectLabelTable.batchInsert(project.labels) { label ->
                    this[ProjectLabelTable.project] = project.name
                    this[ProjectLabelTable.label] = label.name
                }

                ProjectStateTable.batchInsert(project.states) { state ->
                    val trans = mapStates(project.transitions.getOrDefault(state, listOf()))
                    this[ProjectStateTable.project] = project.name
                    this[ProjectStateTable.state] = state.name
                    this[ProjectStateTable.transitions] = trans
                }

                UserProjectTable.insert {
                    it[UserProjectTable.user] = token
                    it[UserProjectTable.projectName] = project.name
                }
            }catch (e : ExposedSQLException){
                throw AlreadyExistsException()
            }
        }
        return project.name
    }

    fun getAll(token : String) : List<Project>{
        return  transaction {
            ProjectTable
                    .innerJoin(UserProjectTable)
                    .select { UserProjectTable.user eq token }
                    .map {
                        val name = it[ProjectTable.name]
                        val labels = getLabels(name)
                        val states = getStates(name)
                        mapResultRow(it,labels, states) }
                    .toList()
        }
    }

    fun get(name : String) : Project?{
        val row = transaction {
            ProjectTable.select { ProjectTable.name.eq(name) }.firstOrNull()
        }
        if(row == null) return null
        val states = getStates(name)
        val labels = getLabels(name)
        return mapResultRow(row,labels, states)

    }

    fun update(new : Project) : Project?{
        val res = transaction {
            try {
                ProjectTable.update({ ProjectTable.name eq new.name}){
                    it[description] = new.description
            }
                updateLabels(new)
            updateStates(new)
        }catch (e : PSQLException){
                 null
            }
        }
        if(res ==  null) return null
        return new
    }

    fun delete(project : String) : String?{
        transaction {
            try {
                ProjectLabelTable.deleteWhere { ProjectLabelTable.project eq project }
                ProjectStateTable.deleteWhere { ProjectStateTable.project eq project }
                val issues = IssueMapper()
                issues.deleteOf(project)
                ProjectTable.deleteWhere { ProjectTable.name eq project }
            }catch (e : PSQLException){
                null
            }
        }
        return project
    }


    private fun mapStates(states : List<State> ) : String{
        val sb = StringBuilder()
        states.forEach { sb.append(it.name + ",") }
        if(sb.isNotEmpty())
            sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    private fun mapResultRow(row: ResultRow, labels : List<Label> , states : List<State>): Project {
        return Project(
                row[ProjectTable.name],
                row[ProjectTable.description],
                states,
                labels,
                getTransitions(row[ProjectTable.name]))
    }

    private fun getStates(project : String) : List<State> {
        val states = ArrayList<State>()
        transaction {
            (ProjectTable innerJoin ProjectStateTable)
                    .slice(ProjectStateTable.state)
                    .select { ProjectTable.name eq project }
                    .map { State.valueOf(it[ProjectStateTable.state]) }
                    .forEach { states.add(it) }
        }
        return states
    }

    private fun getLabels(project : String) : List<Label> {
        val labels = ArrayList<Label>()
        transaction {
            (ProjectTable innerJoin ProjectLabelTable)
                    .slice(ProjectLabelTable.label)
                    .select { ProjectTable.name.eq(project) }
                    .map { Label.valueOf(it[ProjectLabelTable.label]) }
                    .forEach { labels.add(it) }
        }
        return labels
    }

    private fun getTransitions(project : String) : Map<State, List<State>>{
        val transitions = HashMap<State, List<State>>()
        transaction {
            ProjectStateTable
                    .select {ProjectStateTable.project eq project}
                    .forEach{ row ->
                        val trans = ArrayList<State>()
                        row[ProjectStateTable.transitions]
                                .split(",")
                                .forEach {
                                    if(it.isNotEmpty())
                                        trans.add(State.valueOf(it))
                                }
                        transitions[State.valueOf(row[ProjectStateTable.state])] = trans
                    }
        }
        return transitions
    }

    private fun updateStates(new: Project) {
        transaction {
            ProjectStateTable.deleteWhere { ProjectStateTable.project eq new.name }
            ProjectStateTable.batchInsert(new.states) { state ->
                val trans = new.transitions.getOrDefault(state, listOf())
                this[ProjectStateTable.project] = new.name
                this[ProjectStateTable.state] = state.name
                this[ProjectStateTable.transitions] = mapStates(trans)
            }
        }

    }

    private fun updateLabels(new : Project) {
        transaction {
            ProjectLabelTable
                    .deleteWhere { ProjectLabelTable.project eq new.name }
            ProjectLabelTable.batchInsert(new.labels){ label ->
                this[ProjectLabelTable.project] = new.name
                this[ProjectLabelTable.label] = label.name
            }
        }
    }

}

