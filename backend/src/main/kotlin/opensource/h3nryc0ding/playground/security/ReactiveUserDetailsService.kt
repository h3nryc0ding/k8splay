package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveUserDetailsService(
    private val userRepository: UserRepository,
) : ReactiveUserDetailsService {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findByUsername(username)
            .doOnNext { log.debug("User `${it.username}` loaded from database") }
            .map { it as UserDetails }
    }
}
