@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.chat

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.DgsSubscription
import com.netflix.graphql.dgs.InputArgument
import opensource.h3nryc0ding.playground.generated.types.MessageInput
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.LocalDateTime
import java.util.UUID
import opensource.h3nryc0ding.playground.generated.types.Message as MessageDTO

@Document
data class Message(
    @Id
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val creator: String,
    val timestamp: String,
) {
    fun toDTO() =
        MessageDTO(
            id = { this.id.toString() },
            text = { this.text },
            creator = { this.creator },
            timestamp = { this.timestamp },
        )
}

@Suppress("unused")
@Repository
interface MessageRepository : ReactiveMongoRepository<Message, UUID>

@DgsComponent
class MessageDataFetcher(
    private val messageRepository: MessageRepository,
) {
    private val sink = Sinks.many().multicast().directBestEffort<MessageDTO>()

    @DgsQuery(field = "messages")
    fun getMessages(): Flux<MessageDTO> {
        return messageRepository.findAll().map { it.toDTO() }
    }

    @DgsMutation(field = "messageSend")
    fun sendMessage(
        @InputArgument input: MessageInput,
    ): Mono<MessageDTO> {
        val message =
            Message(
                text = input.text,
                creator = input.creator,
                timestamp = LocalDateTime.now().toString(),
            )
        return messageRepository.save(message).map { it.toDTO() }.doOnNext {
            sink.tryEmitNext(it)
        }
    }

    @DgsSubscription(field = "messageSent")
    fun messageSent(): Flux<MessageDTO> {
        return sink.asFlux()
    }
}
