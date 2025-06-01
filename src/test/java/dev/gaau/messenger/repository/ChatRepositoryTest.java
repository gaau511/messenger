package dev.gaau.messenger.repository;

import dev.gaau.messenger.domain.ChatRoom;
import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.domain.MemberChatRoom;
import dev.gaau.messenger.domain.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChatRepositoryTest {

    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberChatRoomRepository mcrRepository;
    @Autowired MessageRepository messageRepository;

    private Long temporaryRoomId = 1L;
    private Long temporaryMemberId = 1L;

    @Test
    void addMember_ShouldPersistOneMcr() {
        ChatRoom chatRoom = chatRoomRepository.findById(temporaryRoomId).get();
        Member member = memberRepository.findById(5L).get();

        chatRoom.addMember(member);

        List<MemberChatRoom> mcrs = chatRoom.getMemberChatRooms();
        assertThat(mcrs).hasSize(4);
    }

    @Test
    void addMessage_ShouldPersistOneMessage() {
        ChatRoom chatRoom = chatRoomRepository.findById(temporaryRoomId).get();
        Member member = memberRepository.findById(temporaryMemberId).get();

        Message message = new Message("USER", "Hi!");

        chatRoom.addMessage(message);
        member.addMessage(message);

        int unreadCount = chatRoom.getMemberChatRooms().size();
        message.setUnreadCount(unreadCount);
        Message savedMessage = messageRepository.save(message);

        assertThat(savedMessage.getUnreadCount()).isEqualTo(3);
        assertThat(chatRoom.getMessages()).containsOnlyOnce(savedMessage);
        assertThat(member.getMessages()).containsOnlyOnce(savedMessage);
    }
}
