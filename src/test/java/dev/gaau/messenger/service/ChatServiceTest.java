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

    @Test
    void createChatRoom_ShouldSuccess() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto("ChatRoom for test", List.of(2L, 3L));
        ChatRoomDto chatRoomDto = chatService.createChatRoom(1L, testDto);
        assertThat(chatRoomDto.getTitle()).isEqualTo("ChatRoom for test");
        assertThat(chatRoomDto.getParticipantsId()).hasSize(3);
        assertThat(chatRoomDto.getParticipantsId()).contains(1L, 2L, 3L);
    }

    @Test
    void createChatRoom_ShouldFail_WhenMemberIsNotExist() {
        ChatRoomCreateRequestDto testDto = new ChatRoomCreateRequestDto("ChatRoom for test", List.of(8L, 9L));

        assertThatThrownBy(() -> chatService.createChatRoom(1L, testDto))
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    void sendMessage_ShouldSuccess() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        MessageDto messageDto = chatService.sendMessage(1L, 1L, testDto);
        assertThat(messageDto.getChatRoomId()).isEqualTo(1L);
        assertThat(messageDto.getContents()).isEqualTo("Hi! How are you doing?");
        assertThat(messageDto.getMemberId()).isEqualTo(1L);
        assertThat(messageDto.getReadCount()).isEqualTo(3);
    }

    @Test
    void sendMessage_ShouldFail_WhenChatRoomIsNotExist() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        assertThatThrownBy(
                () -> chatService.sendMessage(1L, 10L, testDto)
        )
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void sendMessage_ShouldFail_WhenSenderIsNotParticipant() {
        MessageRequestDto testDto = new MessageRequestDto("USER", "Hi! How are you doing?");
        assertThatThrownBy(
                () -> chatService.sendMessage(4L, 1L, testDto)
        )
                .isInstanceOf(RuntimeException.class);
    }

}