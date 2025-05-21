package dev.gaau.messenger.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private List<Long> participantsId;
}
