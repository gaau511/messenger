package dev.gaau.messenger.dto.request;


import lombok.*;

@Getter
@AllArgsConstructor
public class MessageRequestDto {
    private Long requesterId;
    private Long chatRoomId;
    private String type;
    private String contents;
}
