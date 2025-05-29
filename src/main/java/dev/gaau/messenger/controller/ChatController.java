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
@RequestMapping("/view/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final Long temporaryMemberId = 1L;

    @GetMapping("/create")
    public String createChatRoomPage (Model model) {
        model.addAttribute("members", chatService.getMembers());
        return "chat/create";
    }

    @PostMapping("/create")
    public String createChatRoom(@ModelAttribute ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        ChatRoomDto chatRoom = chatService.createChatRoom(temporaryMemberId, chatRoomCreateRequestDto);
        return "redirect:room/" + chatRoom.getId();
    }

    @GetMapping("/list")
    public String lookUpChatRoomList(Model model) {
        List<ChatRoomSummaryDto> chatRooms = chatService.getChatRoomsByMemberId(temporaryMemberId);
        model.addAttribute("chatRooms", chatRooms);
        return "chat/list";
    }
}
