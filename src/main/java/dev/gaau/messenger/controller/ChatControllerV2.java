package dev.gaau.messenger.controller;

import dev.gaau.messenger.dto.response.ChatRoomDetailDto;
import dev.gaau.messenger.dto.response.MessageDto;
import dev.gaau.messenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/view/v2/chats")
@RequiredArgsConstructor
public class ChatControllerV2 {

    private final ChatService chatService;

    @GetMapping("/{chatRoomId}/{memberId}")
    public String chatRoomPage(Model model,
                               @PathVariable("memberId") Long memberId,
                               @PathVariable("chatRoomId") Long chatRoomId) {

        ChatRoomDetailDto chatRoomDetail = chatService.getChatRoomById(chatRoomId);
        List<MessageDto> messages = chatService.getMessages(memberId, chatRoomId);

        model.addAttribute("chatRoom", chatRoomDetail);
        model.addAttribute("loggedInMemberId", memberId);
        return "socket/room";
    }
}
