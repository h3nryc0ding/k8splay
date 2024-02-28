package opensource.h3nryc0ding.playground.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Value("\${PUBLIC_FRONTEND_DOMAIN}")
    lateinit var baseDomain: String

    private fun createCorsFilter(origin: String): CorsWebFilter {
        val config =
            CorsConfiguration().apply {
                allowCredentials = false
                addAllowedOrigin(origin)
                addAllowedHeader("*")
                addAllowedMethod("*")
            }
        val source =
            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**", config)
            }
        return CorsWebFilter(source)
    }

    @Bean
    @Profile("prod")
    fun corsFilterProd(): CorsWebFilter {
        return createCorsFilter("https://$baseDomain")
    }

    @Bean
    @Profile("!prod")
    fun corsFilter(): CorsWebFilter {
        @Suppress("HttpUrlsUsage")
        return createCorsFilter("http://$baseDomain")
    }
}
