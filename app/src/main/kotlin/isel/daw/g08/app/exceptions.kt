package isel.daw.g08.app

import isel.daw.g08.app.issue.State


class AlreadyExistsException : Exception()
class ResourceNotFoundException(val resource : String, val id : String) : Exception()
class InvalidIssueStateTransition(val currentState : String, val targetState : String, val validStates : List<State>) : Exception()