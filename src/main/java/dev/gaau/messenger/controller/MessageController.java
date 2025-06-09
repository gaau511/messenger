package dev.gaau.messenger.controller;

import dev.gaau.messenger.dto.request.MessageRequestDto;
import dev.gaau.messenger.dto.response.MessageDto;
import dev.gaau.messenger.service.ChatService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations template;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(MessageRequestDto messageRequestDto) {
        MessageDto message = chatService.sendMessage(messageRequestDto);
        template.convertAndSend("/topic/chat/room/" + message.getChatRoomId(), message);
    }

}
