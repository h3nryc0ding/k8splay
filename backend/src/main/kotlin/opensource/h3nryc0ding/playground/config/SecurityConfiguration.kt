package opensource.h3nryc0ding.playground.config

import opensource.h3nryc0ding.playground.security.ReactiveAuthenticationManager
import opensource.h3nryc0ding.playground.security.UnauthorizedAuthenticationEntryPoint
import opensource.h3nryc0ding.playground.security.header.AuthenticationHeaderConverter
import opensource.h3nryc0ding.playground.security.header.AuthenticationHeaderMatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfiguration {
    companion object {
        val GRAPHQL_WHITELIST = arrayOf("/graphql/**", "/graphiql/**")
    }

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        entryPoint: UnauthorizedAuthenticationEntryPoint,
        authFilter: AuthenticationWebFilter,
    ): SecurityWebFilterChain {
        return http {
            httpBasic { disable() }
            formLogin { disable() }
            csrf { disable() }
            logout { disable() }

            exceptionHandling {
                authenticationEntryPoint = entryPoint
            }

            authorizeExchange {
                authorize(pathMatchers(*GRAPHQL_WHITELIST), permitAll)
                authorize(anyExchange, authenticated)
            }

            addFilterAt(
                authFilter,
                SecurityWebFiltersOrder.AUTHORIZATION,
            )
        }
    }

    @Bean
    fun webFilter(
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        exchangeMatcher: AuthenticationHeaderMatcher,
        authConverter: AuthenticationHeaderConverter,
    ): AuthenticationWebFilter {
        return AuthenticationWebFilter(reactiveAuthenticationManager).apply {
            setRequiresAuthenticationMatcher(exchangeMatcher)
            setServerAuthenticationConverter(authConverter)
            setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
