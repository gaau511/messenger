package dev.gaau.messenger.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomCreateRequestDto {
    private String title;
    private Long requesterId;
    private List<Long> memberIds;
}
