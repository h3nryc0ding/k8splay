package opensource.h3nryc0ding.playground.user

import opensource.h3nryc0ding.playground.security.JWTReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.JWTToken
import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/authenticate")
class UserController(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: JWTReactiveAuthenticationManager,
) {
    @RequestMapping(value = [""], method = [RequestMethod.POST])
    fun authorize(
        @RequestBody login: LoginDTO,
        response: ServerHttpResponse,
    ): Mono<JWTToken> {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(login.username, login.password)

        val authentication = authenticationManager.authenticate(authenticationToken)
        // TODO: Value is never used as Publisher (onErrorMap)
        authentication.onErrorMap {
            throw BadCredentialsException("Bad crendentials")
        }
        ReactiveSecurityContextHolder.withAuthentication(authenticationToken)

        return authentication.map {
            JWTToken(tokenProvider.createToken(it))
        }
    }

    data class LoginDTO(
        val username: String,
        val password: String,
    )
}
