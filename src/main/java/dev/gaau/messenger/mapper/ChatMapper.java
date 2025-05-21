package dev.gaau.messenger.mapper;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.domain.Message;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.dto.response.MessageDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public ChatRoomDto chatRoomToChatRoomDto(ChatRoom chatRoom) {

        List<Long> participantsId = chatRoom.getMemberChatRooms().stream().map(
                memberChatRoom -> memberChatRoom.getMember().getId()
        ).toList();

        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .createdAt(chatRoom.getCreatedAt())
                .title(chatRoom.getTitle())
                .participantsId(participantsId)
                .build();
    }

    public Message toMessage(MessageRequestDto messageRequestDto, ChatRoom chatRoom,
                             Member member) {
        return Message.builder()
                .type(messageRequestDto.getType())
                .contents(messageRequestDto.getContents())
                .chatRoom(chatRoom)
                .member(member)
                .readCount(chatRoom.getMemberChatRooms().size())
                .build();
    }

    public MessageDto messageToMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .type(message.getType())
                .contents(message.getContents())
                .createdAt(message.getCreatedAt())
                .chatRoomId(message.getChatRoom().getId())
                .memberId(message.getMember().getId())
                .readCount(message.getChatRoom().getMemberChatRooms().size())
                .build();
    }
}
