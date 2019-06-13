package isel.daw.g08.app

sealed class Result
class AlreadyExists : Result()
class Created(val id : String) : Result()