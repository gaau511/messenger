package dev.gaau.messenger.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomSummaryDto {
    private Long id;
    private String title;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private List<String> participantsName;
}
