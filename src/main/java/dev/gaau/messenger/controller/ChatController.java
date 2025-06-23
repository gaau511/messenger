package dev.gaau.messenger.controller;

import dev.gaau.messenger.dto.request.ChatRoomCreateRequestDto;
import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.ChatRoomDetailDto;
import dev.gaau.messenger.dto.response.ChatRoomInfoDto;
import dev.gaau.messenger.dto.response.ChatRoomSummaryDto;
import dev.gaau.messenger.dto.response.MessageDto;
import dev.gaau.messenger.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/view/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public String lookUpChatRoomList(Model model,
                                     @PathVariable("memberId") Long memberId) {
        List<ChatRoomSummaryDto> chatRooms = chatService.getChatRoomsByMemberId(memberId);
        model.addAttribute("chatRooms", chatRooms);
        return "chat/list";
    }

    @GetMapping("/create/{memberId}")
    public String createChatRoomPage (Model model,
                                      @PathVariable("memberId") Long memberId) {
        model.addAttribute("members", chatService.getMembers());
        model.addAttribute("loggedInMemberId", memberId);
        return "chat/create";
    }

    @PostMapping("/create")
    public String createChatRoom(@ModelAttribute ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        ChatRoomInfoDto chatRoomInfo = chatService.createChatRoom(chatRoomCreateRequestDto);
        return "redirect:room/" + chatRoomInfo.getId();
    }

    @GetMapping("/{chatRoomId}/{memberId}")
    public String chatRoomPage(Model model,
                               @PathVariable("memberId") Long memberId,
                               @PathVariable("chatRoomId") Long chatRoomId) {

        ChatRoomDetailDto chatRoomDetail = chatService.getChatRoomById(chatRoomId);
        model.addAttribute("loggedInMemberId", memberId);
        model.addAttribute("chatRoom", chatRoomDetail);

        return "chat/room";
    }

    @PostMapping("/{chatRoomId}")
    public String sendMessage(@PathVariable("chatRoomId") Long chatRoomId,
                              @ModelAttribute MessageRequestDto messageRequestDto) {
        MessageDto messageDto = chatService.sendMessage(messageRequestDto);

        return "redirect:" + chatRoomId;
    }
}
