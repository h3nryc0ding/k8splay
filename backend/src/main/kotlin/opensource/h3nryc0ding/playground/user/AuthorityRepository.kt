package opensource.h3nryc0ding.playground.user

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository : ReactiveMongoRepository<Authority, String>
