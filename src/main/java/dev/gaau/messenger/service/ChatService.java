package dev.gaau.messenger.service;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.domain.MemberChatRoom;
import dev.gaau.messenger.domain.Message;
import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.dto.response.ChatRoomSummaryDto;
import dev.gaau.messenger.dto.response.MessageDto;
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
    private final MessageRepository messageRepository;
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

        // 6. 채팅방 이름 생성
        String title = chatRoomCreateRequestDto.getTitle() == null ? makeDefaultChatRoomTitle(participants) : chatRoomCreateRequestDto.getTitle();

        // 6. ChatRoom 엔티티 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .title(title)
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

    private String makeDefaultChatRoomTitle(List<Member> participants) {
        return String.join(", ", participants.stream().map(Member::getName).toList());
    }

    public MessageDto sendMessage(Long loggedInMemberId, Long chatRoomId, MessageRequestDto messageRequestDto) {

        // 1. 로그인한 사용자의 ID로 Member 엔티티 조회
        Member loggedInMember = memberRepository.findById(loggedInMemberId).orElseThrow(
                () -> new RuntimeException("cannot find member with id : " + loggedInMemberId)
        );

        // 2. 채팅방 ID로 ChatRoom 엔티티 조회
        ChatRoom savedChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new RuntimeException("cannot find chat room with id : " + chatRoomId)
        );

        // 3. 로그인한 사용자가 채팅방의 participants 인지 확인
        if (!memberChatRoomRepository.existsByMemberAndChatRoom(loggedInMember,savedChatRoom)) {
            throw new RuntimeException("Member " + loggedInMemberId + " is not the participant of Chat Room " + chatRoomId);
        }

        // 4. 요청 DTO로부터 message 생성
        Message message = chatMapper.toMessage(messageRequestDto, savedChatRoom, loggedInMember);

        // 5. message 저장
        Message savedMessage = messageRepository.save(message);

        // 6. ChatRoom에 저장된 메시지 저장
        savedChatRoom.getMessages().add(savedMessage);

        // 7. ChatRoom에 마지막 메시지 ID 업데이트
        savedChatRoom.setLastMessage(savedMessage);

        // 8. Message 엔티티를 응답 DTO로 변환하여 반환
        return chatMapper.messageToMessageDto(savedMessage);
    }

    public List<MessageDto> getMessages(Long loggedInMemberId, Long chatRoomId) {
        // 1. 로그인한 사용자의 ID로 Member 엔티티 조회
        Member loggedInMember = memberRepository.findById(loggedInMemberId).orElseThrow(
                () -> new RuntimeException("cannot find member with id : " + loggedInMemberId)
        );

        // 2. 채팅방 ID로 ChatRoom 엔티티 조회
        ChatRoom savedChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new RuntimeException("cannot find chat room with id : " + chatRoomId)
        );

        // 3. 로그인한 사용자가 채팅방의 participants 인지 확인
        if (!memberChatRoomRepository.existsByMemberAndChatRoom(loggedInMember,savedChatRoom)) {
            throw new RuntimeException("Member " + loggedInMemberId + " is not the participant of Chat Room " + chatRoomId);
        }

        // 4. 조회된 채팅방에서 message list 조회
        List<Message> messages = savedChatRoom.getMessages();

        // 5. Message list를 응답 DTO로 변환하여 반환
        return messages.stream().map(chatMapper::messageToMessageDto).toList();
    }

    public List<ChatRoomSummaryDto> getChatRoomsByMemberId(Long loggedInMemberId) {
        // 1. 로그인한 사용자의 ID로 Member 엔티티 조회
        Member loggedInMember = memberRepository.findById(loggedInMemberId).orElseThrow(
                () -> new RuntimeException("cannot find member with id : " + loggedInMemberId)
        );

        List<ChatRoomSummaryDto> chatRoomSummaryDtoList = new ArrayList<>();
        // 2. 회원이 참여하고 있는 채팅방 조회
        for (MemberChatRoom mcr : loggedInMember.getMemberChatRooms()) {

            ChatRoomSummaryDto chatRoomSummaryDto = new ChatRoomSummaryDto();
            List<String> participantsName = new ArrayList<>();
            ChatRoom chatRoom = mcr.getChatRoom();

            // 2-1. 해당 채팅방의 참여자 이름 조회
            for(MemberChatRoom mcr2 : chatRoom.getMemberChatRooms()) {
                participantsName.add(mcr2.getMember().getName());
            }

            // 2-2 chatRoomSummaryDto 생성
            chatRoomSummaryDto.setId(chatRoom.getId());
            chatRoomSummaryDto.setParticipantsName(participantsName);
            chatRoomSummaryDto.setTitle(chatRoom.getTitle());
            if (chatRoom.getLastMessage() != null) {
                chatRoomSummaryDto.setLastMessage(chatRoom.getLastMessage().getContents());
                chatRoomSummaryDto.setLastMessageTime(chatRoom.getLastMessage().getCreatedAt());
            }
            chatRoomSummaryDtoList.add(chatRoomSummaryDto);
        }

        return chatRoomSummaryDtoList;
    }

}

