package io.github.angelogalvao.example.ai.web;

import io.github.angelogalvao.example.ai.model.Evaluation;
import io.github.angelogalvao.example.ai.service.SentimentAnalysis;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.inject.Inject;

import java.util.List;

@WebSocket(path = "/chat")
public class WebSocketChatBot {

    @Inject
    SentimentAnalysis sentimentAnalysis;

    @OnTextMessage
    public String onMessage(String message) {
        Evaluation evaluation = sentimentAnalysis.triage(List.of(Evaluation.values()), message);

        return evaluation.name();
    }
}
