package dev.gaau.messenger.mapper;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatRoomDto chatRoomToChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .createdAt(chatRoom.getCreatedAt())
                .title(chatRoom.getTitle())
                .build();
    }
}
