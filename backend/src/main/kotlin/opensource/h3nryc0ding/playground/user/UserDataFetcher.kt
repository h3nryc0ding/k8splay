package opensource.h3nryc0ding.playground.user

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import opensource.h3nryc0ding.playground.generated.types.AuthenticationInput
import opensource.h3nryc0ding.playground.security.ReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import org.springframework.security.core.userdetails.User


@DgsComponent
class UserDataFetcher(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: ReactiveAuthenticationManager,
) {
    @DgsMutation
    fun authenticate(
        @InputArgument input: AuthenticationInput,
    ): Mono<String> {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(input.username, input.password)

        return authenticationManager.authenticate(authenticationToken)
            .map { authentication ->
                ReactiveSecurityContextHolder.withAuthentication(authentication)
                tokenProvider.createToken(authentication)
            }
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun currentUser(dfe: DgsDataFetchingEnvironment): Mono<User> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal as User }
    }
}
