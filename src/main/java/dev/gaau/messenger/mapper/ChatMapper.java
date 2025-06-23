package dev.gaau.messenger.mapper;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.domain.Message;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public Message messageRequestDtoToMessage(MessageRequestDto messageRequestDto) {
        return new Message(
                messageRequestDto.getType(),
                messageRequestDto.getContents()
        );
    }

    public MessageDto messageToMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getType(),
                message.getCreatedAt(),
                message.getUnreadCount(),
                message.getContents(),
                message.getChatRoom().getId(),
                message.getMember().getId()
        );
    }

    public MemberInfoDto memberToMemberInfoDto(Member member) {
        return new MemberInfoDto(
                member.getId(),
                member.getUsername(),
                member.getNickname()
        );
    }

    public ChatRoomInfoDto chatRoomToChatRoomInfoDto(ChatRoom chatRoom) {

        List<MemberInfoDto> memberInfos = chatRoom.getMemberChatRooms().stream().map(
                mcr -> memberToMemberInfoDto(mcr.getMember())
        ).toList();

        return new ChatRoomInfoDto(
                chatRoom.getId(),
                chatRoom.getTitle(),
                memberInfos
        );
    }

    public ChatRoomSummaryDto chatRoomToChatRoomSummaryDto(ChatRoom chatRoom) {

        if (chatRoom.getLastMessage() == null)
            return new ChatRoomSummaryDto(
                    chatRoomToChatRoomInfoDto(chatRoom),
                    null
            );

        return new ChatRoomSummaryDto(
                chatRoomToChatRoomInfoDto(chatRoom),
                messageToMessageDto(chatRoom.getLastMessage())
        );
    }

    public ChatRoomDetailDto chatRoomToChatRoomDetailDto(ChatRoom chatRoom) {

        List<MemberInfoDto> memberInfos = chatRoom.getMemberChatRooms().stream().map(
                mcr -> memberToMemberInfoDto(mcr.getMember())
        ).toList();

        List<MessageDto> messages = chatRoom.getMessages().stream().map(this::messageToMessageDto).toList();

        return new ChatRoomDetailDto(
                chatRoomToChatRoomInfoDto(chatRoom),
                memberInfos,
                messages
        );
    }
}
