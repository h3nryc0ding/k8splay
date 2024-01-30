package opensource.h3nryc0ding.playground.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "user")
data class CustomUser(
    @Id
    val id: String,
    @Indexed
    val login: String,
    val password: String,
    val authorities: Set<Authority> = HashSet(),
) {
    fun toUserDetails(): UserDetails {
        return User(
            login,
            password,
            authorities,
        )
    }
}
