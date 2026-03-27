package io.github.angelogalvao.example.ai.resource;

import io.github.angelogalvao.example.ai.service.ThemeParkChatBot;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.inject.Inject;

@WebSocket(path = "/chat")
public class WebSocketChatBot {

    @Inject
    ThemeParkChatBot themeParkChatBot;

    @OnOpen
    public String onOpen(){
        return  "Hello, how can I help you?";
    }

    @OnTextMessage
    public String onMessage(String message){

        return themeParkChatBot.chat(message);
    }
}
