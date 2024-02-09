@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.user

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.reactive.internal.DgsReactiveRequestData
import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.ReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.TokenProvider
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono
import java.time.Duration
import opensource.h3nryc0ding.playground.generated.types.User as UserDTO

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
    ): Mono<UserDTO> {
        log.info("Attempting to authenticate user: `${input.username}`.")
        val authenticationToken = UsernamePasswordAuthenticationToken(input.username, input.password)

        return authenticateAndRespond(authenticationToken, dfe)
    }

    @DgsData(parentType = "Mutation", field = "userRegister")
    fun userRegister(
        @InputArgument input: AuthenticationInput,
        dfe: DgsDataFetchingEnvironment,
    ): Mono<UserDTO> {
        log.info("Attempting to register user: `${input.username}`.")
        val user = CustomUser(input.username, input.password)

        return userRepository.save(user)
            .flatMap {
                val authenticationToken = UsernamePasswordAuthenticationToken(it.login, it.password)
                authenticateAndRespond(authenticationToken, dfe)
            }
    }

    @DgsData(parentType = "Query", field = "currentUser")
    @PreAuthorize("isAuthenticated()")
    fun currentUser(dfe: DgsDataFetchingEnvironment): Mono<UserDTO> {
        return ReactiveSecurityContextHolder.getContext()
            .map { createDTO(it.authentication) }
    }

    @DgsData(parentType = "Mutation", field = "userLogout")
    fun userLogout(dfe: DgsDataFetchingEnvironment): Mono<Boolean> {
        return Mono.just(true)
            .doOnNext {
                getResponse(dfe)
                    .addCookie(tokenProvider.createCookie("", COOKIE_CLEAR_DURATION))
            }
    }

    private fun authenticateAndRespond(
        authenticationToken: UsernamePasswordAuthenticationToken,
        dfe: DgsDataFetchingEnvironment,
    ): Mono<UserDTO> {
        return authenticationManager.authenticate(authenticationToken)
            .doOnNext {
                ReactiveSecurityContextHolder.withAuthentication(it)
                log.info("User `${(it.details as UserDetails).username}` authenticated successfully.")
            }
            .map { createDTO(it) }
            .doOnNext {
                getResponse(dfe)
                    .addCookie(tokenProvider.createCookie(it.token))
            }
    }

    private fun createDTO(authentication: Authentication): UserDTO {
        return UserDTO(
            username = { (authentication.details as UserDetails).username },
            token = { tokenProvider.createToken(authentication) },
            authorities = { authentication.authorities.map { it.toString() } },
        )
    }

    private fun getResponse(dfe: DgsDataFetchingEnvironment): ServerHttpResponse {
        return (dfe.getDgsContext().requestData as DgsReactiveRequestData)
            .serverRequest!!
            .exchange()
            .response
    }
}
