package isel.daw.g08.app.auth

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class AuthenticationRequired

@AuthenticationRequired
@Target(AnnotationTarget.FUNCTION)
annotation class AccessRequired