package dev.gaau.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomDetailDto {
    private ChatRoomInfoDto chatRoomInfo;
    private List<MemberInfoDto> participantInfos;
    private List<MessageDto> messages;
}
