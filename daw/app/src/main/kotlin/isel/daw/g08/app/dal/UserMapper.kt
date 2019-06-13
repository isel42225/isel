package isel.daw.g08.app.dal

import isel.daw.g08.app.ResourceNotFoundException
import isel.daw.g08.app.auth.ForbiddenAccessException
import isel.daw.g08.app.auth.InvalidPasswordException
import isel.daw.g08.app.auth.UserNotFoundException
import isel.daw.g08.app.user.ReqUser
import isel.daw.g08.app.user.UserRepr
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.Base64
import java.lang.Exception

class UserMapper {

    fun create(new : ReqUser) : String {

        val usrHash = new.name.encode()
        val pwHash = new.password.encode()
        val token = ("$usrHash:$pwHash").encode()

        transaction {
            UserTable.insert {
                it[UserTable.user] = usrHash
                it[UserTable.password] = pwHash
                it[UserTable.token] = token
            }
        }

        return token
    }

    fun get(username : String, password : String) : UserRepr {
        val usrHash = username.encode()
        val pwHash = password.encode()
        val token = ("$usrHash:$pwHash").encode()
        transaction {
            val query = UserTable.select { UserTable.token eq token }
            val credentials = token.decode().split(":")
            val reqUser = credentials.first()
            val reqPassword = credentials.last()

            if (query.empty()) {
                throw UserNotFoundException(reqUser)
            }

            val row = query.first()
            val realPassword = row[UserTable.password]

            if (!reqPassword.equals(realPassword)) {
                throw InvalidPasswordException()
            }
        }
        return UserRepr(token)
    }

    fun isValid(token : String) : Boolean {
        transaction {
            val query = UserTable.select { UserTable.token eq token }
            val credentials = token.decode().split(":")
            val reqUser = credentials.first().decode()
            val reqPassword = credentials.last().decode()

            if (query.empty()) {
                throw UserNotFoundException(reqUser)
            }

            val row = query.first()
            val realPassword = row[UserTable.password].decode()

            if (!reqPassword.equals(realPassword)) {
                throw InvalidPasswordException()
            }
        }
        return true
    }
    fun hasAccess(token : String, project : String) : Boolean {
         transaction {
            if(project.isEmpty()){
                val userQuery = UserTable.select{UserTable.token eq token}
                if(userQuery.empty()){
                    val user = token.decode().split(":").first()
                    throw UserNotFoundException(user)
                }
            }
            else{
                val projectQuery = UserProjectTable.select { UserProjectTable.projectName eq project }
                if (projectQuery.empty()) throw ResourceNotFoundException("Project", project)
                val projectOwner = projectQuery.first()[UserProjectTable.user]
                if (!projectOwner.equals(token)) {
                    throw ForbiddenAccessException()
                }
            }
        }
        return true
    }

    private fun String.encode() = Base64.encodeBytes(this.toByteArray())
    private fun String.decode() = Base64.decode(this).toString(Charsets.UTF_8)
}