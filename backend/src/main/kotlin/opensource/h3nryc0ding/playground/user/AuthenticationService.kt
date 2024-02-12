package opensource.h3nryc0ding.playground.user

import com.mongodb.DuplicateKeyException
import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.util.toDTO
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import opensource.h3nryc0ding.playground.generated.types.Authentication as AuthenticationDTO

@Service
class AuthenticationService(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun authenticate(input: AuthenticationInput): Mono<AuthenticationDTO> {
        val authenticationToken = UsernamePasswordAuthenticationToken(input.username, input.password)
        return authenticationManager.authenticate(authenticationToken)
            .map { it.toDTO(tokenProvider) }
    }

    fun register(input: AuthenticationInput): Mono<AuthenticationDTO> {
        val hash = passwordEncoder.encode(input.password)
        val user = CustomUser(input.username, hash)
        return userRepository.insert(user)
            .flatMap {
                authenticate(input)
            }
            .onErrorResume(DuplicateKeyException::class.java) {
                Mono.error(UserAlreadyExistsException(input.username))
            }
    }
}
