package opensource.h3nryc0ding.playground.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class JWTReactiveAuthenticationManager(
        private val userDetailsService: ReactiveUserDetailsService,
        private val passwordEncoder: PasswordEncoder,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if (authentication.isAuthenticated) {
            return Mono.just(authentication)
        }

        val authToken =
                authentication as? UsernamePasswordAuthenticationToken
                        ?: return Mono.error(BadCredentialsException("Invalid Credentials"))

        return userDetailsService.findByUsername(authToken.name)
                // TODO: should we publishOn(Schedulers.parallel())?
                .filter { userDetails -> passwordEncoder.matches(authToken.credentials as String, userDetails.password) }
                .switchIfEmpty { Mono.error(BadCredentialsException("Invalid Credentials")) }
                .map {
                    userDetails ->
                    UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
                }
    }
}