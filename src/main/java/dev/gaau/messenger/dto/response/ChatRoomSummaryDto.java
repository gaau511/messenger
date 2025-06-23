package dev.gaau.messenger.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomSummaryDto {
    private ChatRoomInfoDto chatRoomInfo;
    private MessageDto lastMessage;
}
