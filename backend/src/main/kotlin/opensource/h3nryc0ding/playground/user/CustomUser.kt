package opensource.h3nryc0ding.playground.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Document(collection = "user")
data class CustomUser(
    @Id
    @Indexed(unique = true)
    private val _username: String,
    private val _password: String,
    private val _authorities: Set<Authority> = HashSet(),
    private val _accountNonExpired: Boolean = true,
    private val _accountNonLocked: Boolean = true,
    private val _credentialsNonExpired: Boolean = true,
    private val _enabled: Boolean = true,
) : UserDetails {
    override fun getUsername() = _username

    override fun getPassword() = _password

    override fun getAuthorities() = _authorities

    override fun isAccountNonExpired() = _accountNonExpired

    override fun isAccountNonLocked() = _accountNonLocked

    override fun isCredentialsNonExpired() = _credentialsNonExpired

    override fun isEnabled() = _enabled
}

@Repository
interface UserRepository : ReactiveMongoRepository<CustomUser, String> {
    fun findByUsername(username: String): Mono<CustomUser>
}
