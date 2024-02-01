package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.user.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveUserDetailsService(
    private val userRepository: UserRepository,
) : ReactiveUserDetailsService {
    override fun findByUsername(login: String): Mono<UserDetails> {
        return userRepository.findByLogin(login)
            .map { it.toUserDetails() }
    }
}
