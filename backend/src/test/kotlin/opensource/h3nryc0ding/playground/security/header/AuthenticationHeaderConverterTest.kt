package opensource.h3nryc0ding.playground.security.header

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpHeaders
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.core.Authentication
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class AuthenticationHeaderConverterTest {
    @Mock
    private lateinit var tokenProvider: TokenProvider

    @InjectMocks
    private lateinit var authenticationHeaderConverter: AuthenticationHeaderConverter

    @Test
    fun `should return Authentication when Authorization header contains valid token`() {
        // Arrange
        val authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjI0NjQwNjQ4LCJleHAiOj"
        val auth = mock(Authentication::class.java)
        `when`(tokenProvider.getAuthentication(authToken)).thenReturn(auth)
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationHeaderConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { resultAuth ->
                assert(resultAuth == auth)
            }
            .verifyComplete()
    }

    @Test
    fun `should return empty Mono when Authorization header is absent or invalid`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationHeaderConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `should return empty Mono when Authorization header is not a Bearer token`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Basic dGVzdDp0ZXN0")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationHeaderConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }

    @Test
    fun `should return empty Mono when Authorization header is invalid`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationHeaderConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }
}
