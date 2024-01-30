package opensource.h3nryc0ding.playground.security

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class JWTReactiveAuthenticationManager(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : ReactiveAuthenticationManager {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if (authentication.isAuthenticated) {
            return Mono.just(authentication)
        }
        return Mono.just(authentication)
            .log()
            .switchIfEmpty { raiseBadCredentials() }
            .cast(UsernamePasswordAuthenticationToken::class.java)
            .flatMap { authenticateToken(it) }
            .publishOn(Schedulers.parallel())
            .onErrorResume { raiseBadCredentials() }
            .filter { passwordEncoder.matches(authentication.credentials as String, it.password) }
            .switchIfEmpty { raiseBadCredentials() }
            .map { UsernamePasswordAuthenticationToken(authentication.principal, authentication.credentials, it.authorities) }
    }

    private fun authenticateToken(authenticationToken: UsernamePasswordAuthenticationToken): Mono<UserDetails> {
        val username = authenticationToken.name

        log.info("checking authentication for user $username")

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            log.info("authenticated user $username, setting security context")
            return userDetailsService.findByUsername(username)
        }
        // TODO: why is this needed?
        return raiseBadCredentials()
    }

    private fun <T> raiseBadCredentials(): Mono<T> {
        return Mono.error(BadCredentialsException("Invalid Credentials"))
    }
}
