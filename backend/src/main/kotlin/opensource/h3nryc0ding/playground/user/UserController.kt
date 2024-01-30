package opensource.h3nryc0ding.playground.user

import com.fasterxml.jackson.databind.ObjectMapper
import opensource.h3nryc0ding.playground.security.JWTReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.TokenProvider
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/api")
class UserController(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: JWTReactiveAuthenticationManager,
    private val objectMapper: ObjectMapper,
) {
    @PostMapping("/authenticate")
    fun authorize(
        @RequestBody login: LoginDTO,
    ): Mono<JWTToken> {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(login.username, login.password)

        return authenticationManager.authenticate(authenticationToken)
            .onErrorMap {
                BadCredentialsException("Bad credentials")
            }
            .map { authentication ->
                ReactiveSecurityContextHolder.withAuthentication(authentication)
                JWTToken(tokenProvider.createToken(authentication))
            }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account")
    fun getAccount(serverWebExchange: ServerWebExchange): Mono<Any> {
        return serverWebExchange.getPrincipal<Principal>().map {
            objectMapper.writeValueAsString(it)
        }
    }

    data class LoginDTO(
        val username: String,
        val password: String,
    )

    data class JWTToken(
        val token: String,
    )
}
