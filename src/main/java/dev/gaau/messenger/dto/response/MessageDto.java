package dev.gaau.messenger.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private String type;
    private LocalDateTime createdAt;
    private Integer readCount;
    private String contents;
    private Long chatRoomId;
    private Long memberId;
}
