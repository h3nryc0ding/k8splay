@file:Suppress("DgsFieldSimplifyingInspector", "DgsDataSimplifyingInspector")

package opensource.h3nryc0ding.playground.user

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import reactor.core.publisher.Mono
import opensource.h3nryc0ding.playground.generated.types.User as UserDTO

@DgsComponent
class UserDataFetcher {
    @DgsData(parentType = "Query", field = "user")
    @PreAuthorize("isAuthenticated()")
    fun user(): Mono<UserDTO> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal }
            .filter { it is DefaultOidcUser }
            .cast(DefaultOidcUser::class.java)
            .map {
                UserDTO(
                    id = { it.subject },
                    name = { it.name },
                    givenName = { it.givenName },
                    familyName = { it.familyName },
                    preferredUsername = { it.preferredUsername },
                    email = { it.email },
                    emailVerified = { it.emailVerified },
                )
            }
    }
}
