package dev.gaau.messenger.service;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.domain.MemberChatRoom;
import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.mapper.ChatMapper;
import dev.gaau.messenger.repository.ChatRoomRepository;
import dev.gaau.messenger.repository.MemberChatRoomRepository;
import dev.gaau.messenger.repository.MemberRepository;
import dev.gaau.messenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final ChatMapper chatMapper;

    public ChatRoomDto createChatRoom(Long loggedInMemberId, ChatRoomCreateRequestDto chatRoomCreateRequestDto) {

        // 1. 로그인한 사용자의 ID로 Member 엔티티 조회
        Member loggedInMember = memberRepository.findById(loggedInMemberId).orElseThrow(
                () -> new RuntimeException("cannot find member with id : " + loggedInMemberId)
        );

        // 2. 요청 DTO로부터 참여자 ID 목록 추출
        List<Long> participantsIds = chatRoomCreateRequestDto.getMemberIds();

        // 3. 참여자들을 담을 리스트 생성
        List<Member> participants = new ArrayList<>();

        // 4. 각 참여자 ID에 대해 Member 엔티티를 조회하여 participants 리스트에 추가
        participantsIds.forEach(memberId -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Cannot find member with id: " + memberId));
            participants.add(member);
        });

        // 5. 로그인한 사용자도 참여자 목록에 추가
        participants.add(loggedInMember);

        // 6. ChatRoom 엔티티 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .title(chatRoomCreateRequestDto.getTitle())
                .build();

        // 7. ChatRoom 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 8. 각 참여자에 대해 MemberChatRoom 엔티티 생성 및 저장
        participants.forEach(member -> {

            // 8-1. MemberChatRoom 엔티티 생성 (참여자와 채팅방 관계 매핑)
            MemberChatRoom memberChatRoom = MemberChatRoom.builder()
                    .chatRoom(savedChatRoom)
                    .member(member)
                    .build();

            // 8-2. MemberChatRoom 저장
            MemberChatRoom savedMemberChatRoom = memberChatRoomRepository.save(memberChatRoom);

            // 8-3. 양방향 연관 관계 설정
            savedChatRoom.getMemberChatRooms().add(savedMemberChatRoom);
            member.getMemberChatRooms().add(savedMemberChatRoom);
        });

        // 9. ChatRoom 엔티티를 DTO로 변환하여 반환
        return chatMapper.chatRoomToChatRoomDto(savedChatRoom);
    }
}

