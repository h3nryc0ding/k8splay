package opensource.h3nryc0ding.playground.user

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<CustomUser, String> {
    fun findByLogin(login: String): Mono<CustomUser>
}
