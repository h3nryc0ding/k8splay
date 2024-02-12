@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.user

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.TokenProvider
import opensource.h3nryc0ding.playground.util.response
import opensource.h3nryc0ding.playground.util.toDTO
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume
import java.time.Duration
import opensource.h3nryc0ding.playground.generated.types.Authentication as AuthenticationDTO

@DgsComponent
class AuthenticationDataFetcher(
    private val tokenProvider: TokenProvider,
    private val authenticationService: AuthenticationService,
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
        return authenticationService.authenticate(input)
            .doOnNext {
                log.info("User: `${input.username}` has been authenticated.")
                dfe.response.addCookie(tokenProvider.createCookie(it.token))
            }
    }

    @DgsData(parentType = "Mutation", field = "userRegister")
    fun userRegister(
        @InputArgument input: AuthenticationInput,
        dfe: DgsDataFetchingEnvironment,
    ): Mono<AuthenticationDTO> {
        log.info("Attempting to register user: `${input.username}`.")
        return authenticationService.register(input)
            .doOnNext {
                log.info("User: `${input.username}` has been registered.")
                dfe.response.addCookie(tokenProvider.createCookie(it.token))
            }
            .onErrorResume(UserAlreadyExistsException::class) { e ->
                log.error(e.message)
                Mono.error(RuntimeException("Registration failed: ${e.message}"))
            }
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
