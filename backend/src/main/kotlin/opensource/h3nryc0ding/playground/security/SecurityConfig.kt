package opensource.h3nryc0ding.playground.security

import opensource.h3nryc0ding.playground.config.AppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig(
    private val appConfig: AppConfig,
) {
    companion object {
        val GRAPHQL_WHITELIST = arrayOf("/graphql/**", "/graphiql/**")
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }

            authorizeExchange {
                authorize(pathMatchers(*GRAPHQL_WHITELIST), permitAll)
                authorize(anyExchange, authenticated)
            }

            oauth2Login {
                authenticationSuccessHandler =
                    RedirectServerAuthenticationSuccessHandler().apply {
                        setLocation(appConfig.frontendUrl.toURI().resolve("/account"))
                    }
            }

            logout {
                logoutHandler = WebSessionServerLogoutHandler()
                logoutSuccessHandler =
                    RedirectServerLogoutSuccessHandler().apply {
                        setLogoutSuccessUrl(appConfig.frontendUrl.toURI())
                    }
            }
        }
    }
}
