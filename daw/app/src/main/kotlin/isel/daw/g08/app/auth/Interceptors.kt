package isel.daw.g08.app.auth


import isel.daw.g08.app.user.IUserStore
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.Enumeration
import java.util.HashMap



class AuthorizationInterceptor(val store : IUserStore) : HandlerInterceptor{

    /**
     * Pre Controller Handler to verify if request has necessary authentication
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod && handler.hasMethodAnnotation(AuthenticationRequired::class.java)){
            val header = request.getHeader("Authorization")

            if(header == null) {
                throw MissingAuthHeaderException()
            }
            val token = header.split(" ").last()
            request.setAttribute("UserToken", token)

            return store.isValid(token)
        }
        return true
    }
}

class AccessInterceptor(val store : IUserStore) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod && handler.hasMethodAnnotation(AccessRequired::class.java)){
            val token = request.getAttribute("UserToken") as String
            val project = request.requestURI.split("/")[2]
            return store.hasAccess(token, project)
        }
        return true
    }

}

