package isel.daw.g08.app.project

import com.fasterxml.jackson.annotation.JsonCreator
import isel.daw.g08.app.issue.Label
import isel.daw.g08.app.issue.State

typealias StateTransition = Map<State, List<State>>

/**
 * Model class and DTO of a Project
 */
data class Project @JsonCreator constructor(
        val name : String,
        val description: String,
        val states : List<State>,
        val labels : List<Label>,
        val transitions : StateTransition)



