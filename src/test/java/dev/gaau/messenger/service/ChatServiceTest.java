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
}