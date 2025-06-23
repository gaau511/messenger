package dev.gaau.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomInfoDto {
    private Long id;
    private String title;
    private List<MemberInfoDto> participantInfos;
}
