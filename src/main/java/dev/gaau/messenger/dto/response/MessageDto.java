package dev.gaau.messenger.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String type;
    private LocalDateTime createdAt;
    private Integer unreadCount;
    private String contents;
    private ChatRoomInfoDto chatRoomInfo;
    private MemberInfoDto memberInfo;
}
