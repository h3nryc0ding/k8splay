package opensource.h3nryc0ding.playground.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Repository

@Document(collection = "authority")
class Authority(
    @Id
    val name: String,
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}

@Repository
interface AuthorityRepository : ReactiveMongoRepository<Authority, String>
