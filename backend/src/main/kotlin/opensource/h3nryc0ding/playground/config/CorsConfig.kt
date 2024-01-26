package opensource.h3nryc0ding.playground.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Value("\${PUBLIC_FRONTEND_DOMAIN}")
    lateinit var baseDomain: String

    private fun createCorsFilter(origin: String): CorsConfigurationSource {
        val config =
            CorsConfiguration().apply {
                allowCredentials = true
                addAllowedOrigin(origin)
                addAllowedHeader("*")
                addAllowedMethod("*")
            }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    @Profile("prod")
    fun corsFilterProd(): CorsConfigurationSource {
        return createCorsFilter("https://$baseDomain")
    }

    @Bean
    @Profile("!prod")
    fun corsFilter(): CorsConfigurationSource {
        @Suppress("HttpUrlsUsage")
        return createCorsFilter("http://$baseDomain")
    }
}
