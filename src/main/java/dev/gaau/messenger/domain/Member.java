package dev.gaau.messenger.domain;

import dev.gaau.messenger.repository.MemberChatRoomRepository;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Message> messages = new ArrayList<>();


    public void addChatRoom(ChatRoom chatRoom) {
        MemberChatRoom mcr = new MemberChatRoom(this, chatRoom);
        memberChatRooms.add(mcr);
        chatRoom.getMemberChatRooms().add(mcr);
    }


    public void addMessage(Message message) {
        messages.add(message);
        message.setMember(this);
    }


    @Builder
    public Member(String username, String password, String name, String gender, String email, String nickname, LocalDate birth) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.nickname = nickname;
        this.birth = birth;
    }

}
