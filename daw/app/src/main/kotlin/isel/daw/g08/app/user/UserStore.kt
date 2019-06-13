package isel.daw.g08.app.user
import isel.daw.g08.app.dal.UserMapper
import org.springframework.stereotype.Service

@Service
class UserStore : IUserStore {
    private val mapper = UserMapper()

    override fun create(new: ReqUser): UserRepr {
        val token = mapper.create(new)
        return UserRepr(token)
    }

    override fun get(req: ReqUser): UserRepr {
        return mapper.get(req.name, req.password)
    }

    override fun isValid(token: String): Boolean {
        return mapper.isValid(token)
    }

    override fun hasAccess(token: String, project: String): Boolean {
       return mapper.hasAccess(token, project)
    }
}