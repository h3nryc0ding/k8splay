package opensource.h3nryc0ding.playground.chat

import opensource.h3nryc0ding.playground.generated.types.MessageInput
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.argThat
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class MessageDataFetcherTest {
    @Mock
    private lateinit var messageRepository: MessageRepository

    @InjectMocks
    private lateinit var messageDataFetcher: MessageDataFetcher

    @Test
    fun `getMessages returns all persisted messages`() {
        // Arrange
        val (_, message) = mockMessage()
        `when`(messageRepository.findAll()).thenReturn(Flux.just(message))

        // Act
        val result = messageDataFetcher.messages()

        // Assert
        StepVerifier.create(result)
            .expectNextMatches { it.text == message.text && it.creator == message.creator }
            .verifyComplete()

        verify(messageRepository, times(1)).findAll()
    }

    @Test
    fun `messageSent emits messages sent with messageSend`() {
        // Arrange
        val (messageInput, message) = mockMessage()
        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        // Act
        val result = messageDataFetcher.messageSent()

        // Assert
        StepVerifier.create(result)
            .then { messageDataFetcher.messageSend(messageInput).subscribe() }
            .expectNextMatches { it.text == messageInput.text && it.creator == messageInput.creator }
            .thenCancel()
            .verify()
    }

    @Test
    fun `sendMessage calls save on messageRepository`() {
        // Arrange
        val (messageInput, message) = mockMessage()
        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        // Act
        messageDataFetcher.messageSend(messageInput).subscribe()

        // Assert
        verify(messageRepository, times(1)).save(
            argThat {
                it.text == messageInput.text && it.creator == messageInput.creator
            },
        )
    }

    @Test
    fun `sendMessage returns the saved message`() {
        // Arrange
        val (messageInput, message) = mockMessage()
        `when`(messageRepository.save(any(Message::class.java))).thenReturn(Mono.just(message))

        // Act
        val result = messageDataFetcher.messageSend(messageInput)

        // Assert
        StepVerifier.create(result)
            .expectNextMatches { it.text == message.text && it.creator == message.creator }
            .verifyComplete()
    }

    private fun mockMessage(): Pair<MessageInput, Message> =
        Pair(
            MessageInput(
                text = "Hello",
                creator = "H3nryC0ding",
            ),
            Message(
                text = "Hello",
                creator = "H3nryC0ding",
            ),
        )
}
