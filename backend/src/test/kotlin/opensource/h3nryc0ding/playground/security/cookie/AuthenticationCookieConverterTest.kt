package opensource.h3nryc0ding.playground.security.cookie

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.http.HttpCookie
import org.springframework.http.ResponseCookie
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.core.Authentication
import reactor.test.StepVerifier
import java.time.Duration

@ExtendWith(MockitoExtension::class)
class AuthenticationCookieConverterTest {
    @Mock
    private lateinit var tokenProvider: TokenProvider

    @InjectMocks
    private lateinit var authenticationHeaderConverter: AuthenticationCookieConverter

    @Test
    fun `should return Authentication when cookie contains valid token`() {
        // Arrange
        val authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjI0NjQwNjQ4LCJleHAiOj"
        val auth = mock(Authentication::class.java)
        `when`(tokenProvider.getAuthentication(authToken)).thenReturn(auth)
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, authToken))
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
    fun `should return empty Mono when cookie is not present`() {
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
    fun `should delete invalid cookie`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, "invalid"))
                .build()
        val expectedCookie =
            ResponseCookie.from(TokenProvider.COOKIE, "")
                .maxAge(Duration.ZERO)
                .build()
        `when`(
            tokenProvider.createCookie("", Duration.ZERO),
        ).thenReturn(expectedCookie)
        val exchange = MockServerWebExchange.from(request)

        // Act
        authenticationHeaderConverter.convert(exchange).block()

        // Assert
        val responseCookies = exchange.response.cookies
        assertThat(responseCookies).containsKey(TokenProvider.COOKIE)
        val newCookie = responseCookies[TokenProvider.COOKIE]?.firstOrNull()
        assertThat(newCookie).isNotNull
        assertThat(newCookie?.value).isEmpty()
        assertThat(newCookie?.maxAge).isEqualTo(Duration.ZERO)
    }

    @Test
    fun `should return empty Mono when cookie is invalid`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, "invalid"))
                .build()
        `when`(
            tokenProvider.createCookie(any(), any()),
        ).thenReturn(mock(ResponseCookie::class.java))
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationHeaderConverter.convert(exchange)

        // Assert
        StepVerifier.create(result)
            .verifyComplete()
    }
}
