package opensource.h3nryc0ding.playground.util

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import opensource.h3nryc0ding.playground.generated.types.Authentication as AuthenticationDTO

fun Authentication.toDTO(tokenProvider: TokenProvider): AuthenticationDTO {
    val userDetails = this.details as UserDetails
    return AuthenticationDTO(
        username = { userDetails.username },
        token = { tokenProvider.createToken(this) },
        authorities = { this.authorities.map { it.toString() } },
    )
}
