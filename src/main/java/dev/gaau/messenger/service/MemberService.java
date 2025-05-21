package dev.gaau.messenger.service;

import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.dto.response.MemberDto;
import dev.gaau.messenger.mapper.MemberMapper;
import dev.gaau.messenger.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Cannot find member with username : " + username)
        );
    }

    public Optional<MemberDto> findById(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if (findMember.isEmpty())
            return Optional.empty();

        return Optional.of(memberMapper.memberToMemberDto(findMember.get()));
    }
}
