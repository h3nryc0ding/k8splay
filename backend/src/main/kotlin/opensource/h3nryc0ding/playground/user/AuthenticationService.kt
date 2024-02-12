package opensource.h3nryc0ding.playground.user

import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.util.toDTO
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import opensource.h3nryc0ding.playground.generated.types.Authentication as AuthenticationDTO

@Service
class AuthenticationService(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userRepository: UserRepository,
) {
    fun authenticate(input: AuthenticationInput): Mono<AuthenticationDTO> {
        val authenticationToken = UsernamePasswordAuthenticationToken(input.username, input.password)
        return authenticationManager.authenticate(authenticationToken)
            .map { it.toDTO(tokenProvider) }
    }

    fun register(input: AuthenticationInput): Mono<AuthenticationDTO> {
        val user = CustomUser(input.username, input.password)
        return userRepository.save(user)
            .flatMap { authenticate(input) }
    }
}
