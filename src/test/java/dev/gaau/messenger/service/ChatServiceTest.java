package dev.gaau.messenger.service;

import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.dto.response.MessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired ChatService chatService;
    private Long temporaryMemberId = 1L;
    private Long temporaryRoomId = 1L;

    @Test
    void createChatRoom_ShouldSuccess() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto("ChatRoom for test", List.of(2L, 3L));
        ChatRoomDto chatRoomDto = chatService.createChatRoom(temporaryMemberId, testDto);
        assertThat(chatRoomDto.getTitle()).isEqualTo("ChatRoom for test");
        assertThat(chatRoomDto.getParticipantsId()).hasSize(3);
        assertThat(chatRoomDto.getParticipantsId()).contains(temporaryMemberId, 2L, 3L);
    }

    @Test
    void createChatRoom_ShouldSuccess_WhenTitleIsNull() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto(null, List.of(2L, 3L));
        ChatRoomDto chatRoomDto = chatService.createChatRoom(temporaryMemberId, testDto);
        assertThat(chatRoomDto.getTitle()).contains("Joe");
    }

    @Test
    void createChatRoom_ShouldFail_WhenMemberIsNotExist() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto("ChatRoom for test", List.of(1000L, 1001L));

        assertThatThrownBy(() -> chatService.createChatRoom(temporaryMemberId, testDto))
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    void createChatRoom_ShouldCreateOneSetOfMcr() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto("ChatRoom for test", List.of(2L, 3L));
        ChatRoomDto chatRoomDto = chatService.createChatRoom(temporaryMemberId, testDto);
    }

    @Test
    void sendMessage_ShouldSuccess() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        MessageDto messageDto = chatService.sendMessage(temporaryMemberId, temporaryRoomId, testDto);
        assertThat(messageDto.getChatRoomId()).isEqualTo(temporaryRoomId);
        assertThat(messageDto.getContents()).isEqualTo("Hi! How are you doing?");
        assertThat(messageDto.getMemberId()).isEqualTo(temporaryMemberId);
        assertThat(messageDto.getReadCount()).isEqualTo(3);
    }

    @Test
    void sendMessage_ShouldFail_WhenChatRoomIsNotExist() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        assertThatThrownBy(
                () -> chatService.sendMessage(temporaryMemberId, 2000L, testDto)
        )
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void sendMessage_ShouldFail_WhenSenderIsNotParticipant() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        assertThatThrownBy(
                () -> chatService.sendMessage(4L, temporaryRoomId, testDto)
        )
                .isInstanceOf(RuntimeException.class);
    }


    @Test
    void getMessages_ShouldSuccess() {
        List<MessageDto> messages = chatService.getMessages(temporaryMemberId, temporaryRoomId);
        assertThat(messages).isNotNull();
    }

    @Test
    void getMessages_ShouldFail_WhenMemberIsNotParticipant() {
        assertThatThrownBy(
                () -> chatService.getMessages(4L, temporaryRoomId)
        )
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    void getMessages_ShouldFail_WhenMemberIsNotExist() {
        assertThatThrownBy(
                () -> chatService.getMessages(10L, temporaryRoomId)
        )
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    void getMessages_ShouldFail_WhenChatRoomIsNotExist() {
        assertThatThrownBy(
                () -> chatService.getMessages(temporaryMemberId, 2000L)
        )
                .isInstanceOf(RuntimeException.class);

    }
}