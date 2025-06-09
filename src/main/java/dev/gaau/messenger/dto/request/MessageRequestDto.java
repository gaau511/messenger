package dev.gaau.messenger.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequestDto {
    private Long memberId;
    private Long chatRoomId;
    private String type;
    private String contents;
}
