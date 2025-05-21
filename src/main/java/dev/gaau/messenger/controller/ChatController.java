package dev.gaau.messenger.controller;

import dev.gaau.messenger.domain.Member;
import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.dto.response.MessageDto;
import dev.gaau.messenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     *  채팅방 생성 요청 API
     *
     * @return
     */
    @PostMapping
    ResponseEntity<ChatRoomDto> requestChat(@AuthenticationPrincipal Member loggedInMember,
                                            @RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        ChatRoomDto newChatRoom = chatService.createChatRoom(loggedInMember.getId(), chatRoomCreateRequestDto);
        return ResponseEntity.ok(newChatRoom);

    }

    /**
     *
     * 메시지 전송 API
     *
     * @param roomId
     * @param messageRequestDto
     * @return
     */
    @PostMapping("/1/{roomId}")
     ResponseEntity<MessageDto> sendMessage(@AuthenticationPrincipal Member loggedInMember,
                                            @PathVariable("roomId") Long roomId,
                                            @RequestBody MessageRequestDto messageRequestDto) {

        MessageDto newMessage = chatService.sendMessage(loggedInMember.getId(), roomId,messageRequestDto);
        return ResponseEntity.ok(newMessage);
    }

    /**
     *
     * 채팅방 메시지 조회 API
     *
     * @param roomId
     * @return
     */
    @GetMapping("/1/{roomId}")
    ResponseEntity<List<MessageDto>> lookUpMessages(@AuthenticationPrincipal Member loggedInMember,
                                                    @PathVariable("roomId") Long roomId) {
        List<MessageDto> messageList = chatService.getMessages(loggedInMember.getId(), roomId);
        return ResponseEntity.ok(messageList);

    }
}
