@file:Suppress("DgsDataSimplifyingInspector", "DgsFieldSimplifyingInspector")

package opensource.h3nryc0ding.playground.chat

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.DgsSubscription
import com.netflix.graphql.dgs.InputArgument
import opensource.h3nryc0ding.playground.generated.types.MessageInput
import org.reactivestreams.Publisher
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.CrudRepository
import reactor.core.publisher.Sinks
import java.time.LocalDateTime
import java.util.UUID
import opensource.h3nryc0ding.playground.generated.types.Message as MessageDTO

@RedisHash("message")
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

interface MessageRepository : CrudRepository<Message, Long>

@DgsComponent
class MessageDataFetcher(
    private val messageRepository: MessageRepository,
) {
    private val sink = Sinks.many().multicast().directBestEffort<MessageDTO>()

    @DgsQuery(field = "messages")
    fun getMessages(): Collection<MessageDTO> {
        return messageRepository.findAll().map { it.toDTO() }
    }

    @DgsMutation(field = "messageSend")
    fun sendMessage(
        @InputArgument input: MessageInput,
    ): MessageDTO {
        val message =
            Message(
                text = input.text,
                creator = input.creator,
                timestamp = LocalDateTime.now().toString(),
            )
        return messageRepository.save(message).toDTO().also {
            sink.tryEmitNext(it)
        }
    }

    @DgsSubscription(field = "messageSent")
    fun messageSent(): Publisher<MessageDTO> {
        return sink.asFlux()
    }
}
