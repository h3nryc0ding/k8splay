package opensource.h3nryc0ding.playground.security

import org.springframework.http.ResponseCookie
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository
import org.springframework.security.oauth2.client.web.server.WebSessionOAuth2ServerAuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class CustomServerAuthorizationRequestRepository : ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private val delegate = WebSessionOAuth2ServerAuthorizationRequestRepository()

    override fun loadAuthorizationRequest(exchange: ServerWebExchange): Mono<OAuth2AuthorizationRequest> {
        return delegate.loadAuthorizationRequest(exchange)
    }

    override fun saveAuthorizationRequest(
        request: OAuth2AuthorizationRequest,
        exchange: ServerWebExchange,
    ): Mono<Void> {
        val redirectUri = exchange.request.queryParams[SecurityConfig.REDIRECT_PARAM_NAME]?.firstOrNull()
        if (redirectUri != null) {
            exchange.response.cookies.add(
                SecurityConfig.REDIRECT_COOKIE_NAME,
                ResponseCookie
                    .from(
                        SecurityConfig.REDIRECT_COOKIE_NAME,
                        redirectUri,
                    )
                    .maxAge(Duration.ofMinutes(10))
                    .build(),
            )
        }
        return delegate.saveAuthorizationRequest(request, exchange)
    }

    override fun removeAuthorizationRequest(exchange: ServerWebExchange): Mono<OAuth2AuthorizationRequest> {
        return delegate.removeAuthorizationRequest(exchange)
    }
}
