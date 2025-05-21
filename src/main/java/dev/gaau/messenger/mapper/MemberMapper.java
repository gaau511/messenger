package dev.gaau.messenger.mapper;

import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.dto.response.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDto memberToMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .createdAt(member.getCreatedAt())
                .birth(member.getBirth())
                .email(member.getEmail())
                .gender(member.getGender())
                .name(member.getName())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
