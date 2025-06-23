package dev.gaau.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoDto {
    private Long id;
    private String username;
    private String nickname;
}
