@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.user

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.ReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.util.response
import opensource.h3nryc0ding.playground.util.toDTO
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import java.time.Duration
import opensource.h3nryc0ding.playground.generated.types.Authentication as AuthenticationDTO

@DgsComponent
class UserDataFetcher(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val userRepository: UserRepository,
) {
    companion object {
        private val COOKIE_CLEAR_DURATION: Duration = Duration.ofDays(-1)
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @DgsData(parentType = "Mutation", field = "userAuthenticate")
    fun userAuthenticate(
        @InputArgument input: AuthenticationInput,
        dfe: DgsDataFetchingEnvironment,
    ): Mono<AuthenticationDTO> {
        log.info("Attempting to authenticate user: `${input.username}`.")
        val authenticationToken = UsernamePasswordAuthenticationToken(input.username, input.password)
        return authenticationManager.authenticate(authenticationToken)
            .map { it.toDTO(tokenProvider) }
            .doOnNext { dfe.response.addCookie(tokenProvider.createCookie(it.token)) }
    }

    @DgsData(parentType = "Mutation", field = "userRegister")
    fun userRegister(
        @InputArgument input: AuthenticationInput,
        dfe: DgsDataFetchingEnvironment,
    ): Mono<AuthenticationDTO> {
        log.info("Attempting to register user: `${input.username}`.")
        val user = CustomUser(input.username, input.password)

        return userRepository.save(user)
            .flatMap {
                val token = UsernamePasswordAuthenticationToken(it.login, it.password)
                authenticationManager.authenticate(token)
            }
            .map { it.toDTO(tokenProvider) }
            .doOnNext { dfe.response.addCookie(tokenProvider.createCookie(it.token)) }
    }

    @DgsData(parentType = "Query", field = "currentUser")
    @PreAuthorize("isAuthenticated()")
    fun currentUser(dfe: DgsDataFetchingEnvironment): Mono<AuthenticationDTO> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.toDTO(tokenProvider) }
    }

    @DgsData(parentType = "Mutation", field = "userLogout")
    fun userLogout(dfe: DgsDataFetchingEnvironment): Mono<Boolean> {
        return Mono.just(true)
            .doOnNext {
                dfe.response
                    .addCookie(tokenProvider.createCookie("", COOKIE_CLEAR_DURATION))
            }
    }
}
