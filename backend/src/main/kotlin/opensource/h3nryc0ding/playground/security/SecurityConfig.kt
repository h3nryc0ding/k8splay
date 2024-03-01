package opensource.h3nryc0ding.playground.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig() {
    companion object {
        val GRAPHQL_WHITELIST = arrayOf("/graphql/**", "/graphiql/**")
        const val REDIRECT_COOKIE_NAME = "REDIRECT_URI"
        const val REDIRECT_PARAM_NAME = "redirect_uri"
    }

    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        customServerAuthorizationRequestRepository: CustomServerAuthorizationRequestRepository,
        oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    ): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }

            authorizeExchange {
                authorize(pathMatchers(*GRAPHQL_WHITELIST), permitAll)
                authorize(anyExchange, authenticated)
            }

            oauth2Login {
                authorizationRequestRepository = customServerAuthorizationRequestRepository
                authenticationSuccessHandler = oAuth2AuthenticationSuccessHandler
            }
        }
    }
}