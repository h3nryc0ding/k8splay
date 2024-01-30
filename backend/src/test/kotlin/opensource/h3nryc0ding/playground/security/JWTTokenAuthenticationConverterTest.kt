package opensource.h3nryc0ding.playground.security

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpHeaders
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.core.Authentication
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class JWTTokenAuthenticationConverterTest {
    @Mock
    private lateinit var tokenProvider: TokenProvider

    @InjectMocks
    private lateinit var jwtTokenAuthenticationConverter: JWTTokenAuthenticationConverter

    @Test
    fun `convert returns Authentication when Authorization header contains valid token`() {
        // Arrange
        val authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjI0NjQwNjQ4LCJleHAiOj"
        val auth = Mockito.mock(Authentication::class.java)
        `when`(tokenProvider.getAuthentication(authToken)).thenReturn(auth)
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtTokenAuthenticationConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { resultAuth ->
                assert(resultAuth == auth)
            }
            .verifyComplete()
    }

    @Test
    fun `convert returns empty Mono when Authorization header is absent or invalid`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtTokenAuthenticationConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `convert returns empty Mono when Authorization header is not a Bearer token`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Basic dGVzdDp0ZXN0")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtTokenAuthenticationConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `convert returns empty Mono when Authorization header is a Bearer token but invalid`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = jwtTokenAuthenticationConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }
}
