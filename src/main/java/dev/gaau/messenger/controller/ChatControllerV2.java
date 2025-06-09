package dev.gaau.messenger.controller;

import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDto;
import dev.gaau.messenger.dto.response.ChatRoomSummaryDto;
import dev.gaau.messenger.dto.response.MessageDto;
import dev.gaau.messenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/view/v2/chat")
@RequiredArgsConstructor
public class ChatControllerV2 {

    private final ChatService chatService;

    @GetMapping("/room/{memberId}/{chatRoomId}")
    public String chatRoomPage(Model model,
                               @PathVariable("memberId") Long memberId,
                               @PathVariable("chatRoomId") Long chatRoomId) {

        ChatRoomDto chatRoom = chatService.getChatRoomById(chatRoomId);
        List<MessageDto> messages = chatService.getMessages(memberId, chatRoomId);

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("messages", messages);
        model.addAttribute("loggedInMemberId", memberId);
        return "socket/room";
    }
}
