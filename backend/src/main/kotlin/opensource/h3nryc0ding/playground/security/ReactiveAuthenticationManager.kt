package opensource.h3nryc0ding.playground.security

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class ReactiveAuthenticationManager(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : ReactiveAuthenticationManager {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if (authentication.isAuthenticated) {
            log.info("User `${authentication.name}` already authenticated. Skip authentication.")
            return Mono.just(authentication)
        }

        val authToken =
            authentication as? UsernamePasswordAuthenticationToken
                ?: return Mono.error<Authentication?>(BadCredentialsException("Invalid Credentials")).doOnError {
                    log.error(it.message)
                }
        log.debug("Try to authenticate user `${authToken.name}`")
        return userDetailsService.findByUsername(authToken.name)
            // TODO: should we publishOn(Schedulers.parallel())?
            .filter { userDetails -> passwordEncoder.matches(authToken.credentials as String, userDetails.password) }
            .doOnNext { userDetails -> log.info("User `${userDetails.username}` authenticated successfully.") }
            .switchIfEmpty {
                Mono.error<UserDetails?>(BadCredentialsException("Invalid Credentials")).doOnError {
                    log.error(it.message)
                }
            }
            .map { userDetails ->
                UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
            }
    }
}
