package dev.gaau.messenger.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String email;
    private String nickname;
    private String role;
    private LocalDate birth;
    private LocalDateTime createdAt;
}
