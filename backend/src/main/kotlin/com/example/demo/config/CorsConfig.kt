package com.example.demo.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    @Profile("dev")
    fun corsFilterDev(): FilterRegistrationBean<*> {
        return corsFilter("http://localhost")
    }

    @Bean
    @Profile("ci")
    fun corsFilterCI(): FilterRegistrationBean<*> {
        return corsFilter("http://localhost")
    }

    @Bean
    @Profile("prod")
    fun corsFilterProd(): FilterRegistrationBean<*> {
        return corsFilter("http://130.61.237.219.nip.io")
    }

    private fun corsFilter(url: String): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin(url)
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return FilterRegistrationBean(CorsFilter(source)).apply {
            this.order = 0
        }
    }
}
