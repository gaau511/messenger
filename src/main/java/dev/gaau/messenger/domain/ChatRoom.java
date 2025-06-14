package dev.gaau.messenger.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "chat_room")
@NoArgsConstructor
public class ChatRoom {

    @Builder
    public ChatRoom(String title) {
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void addMessage(Message message) {
        messages.add(message);
        message.setChatRoom(this);
    }

    public void addMember(Member member) {
        member.addChatRoom(this);
    }

    @Setter
    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();
}
