package opensource.h3nryc0ding.playground.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class OAuth2AuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
    /**
     * Frontend URL, including protocol and port
     */
    @Value("\${app.frontend.url}")
    lateinit var frontendUrl: String

    override fun onAuthenticationSuccess(
        exchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> {
        exchange.exchange.response.statusCode = HttpStatus.FOUND
        exchange.exchange.response.headers.location = URI.create("$frontendUrl/account")
        return Mono.empty()
    }
}
