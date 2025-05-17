package dev.gaau.messenger.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
}
