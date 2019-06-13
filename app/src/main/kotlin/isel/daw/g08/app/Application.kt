package isel.daw.g08.app

import isel.daw.g08.app.repository.DBConnection
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
	DBConnection()
	runApplication<Application>(*args)
}
