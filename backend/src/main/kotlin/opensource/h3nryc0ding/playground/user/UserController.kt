package opensource.h3nryc0ding.playground.user

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @GetMapping("/user")
    fun user(
        @AuthenticationPrincipal principal: OAuth2User,
    ): Any {
        return principal
    }
}
