package opensource.h3nryc0ding.playground.security.cookie

import opensource.h3nryc0ding.playground.security.TokenProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpCookie
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class AuthenticationCookieMatcherTest {
    @InjectMocks
    private lateinit var authenticationCookieMatcher: AuthenticationCookieMatcher

    @Test
    fun `should match when cookie name is equal to authentication cookie name`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, "<cookie>"))
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationCookieMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `should return notMatch when cookie name is not equal to authentication cookie name`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie("cookie", "<cookie>"))
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationCookieMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `should return notMatch when cookie is not present`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationCookieMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `should return notMatch when cookie value is empty`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, ""))
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationCookieMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }

    @Test
    fun `should return notMatch when cookie value is blank`() {
        // Arrange
        val request =
            MockServerHttpRequest.get("/")
                .cookie(HttpCookie(TokenProvider.COOKIE, " "))
                .build()
        val exchange = MockServerWebExchange.from(request)

        // Act
        val result = authenticationCookieMatcher.matches(exchange)

        // Assert
        StepVerifier.create(result)
            .assertNext { matchResult ->
                assert(!matchResult.isMatch)
            }
            .verifyComplete()
    }
}
