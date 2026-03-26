package io.github.angelogalvao.example.ai.triageservice.controller;

import dev.langchain4j.model.chat.ChatModel;
import io.github.angelogalvao.example.ai.triageservice.model.Evaluation;
import io.github.angelogalvao.example.ai.triageservice.service.TriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TriageServiceController {

    @Autowired
    TriageService triageService;

    @Autowired
    ChatModel model;

    @GetMapping("/capital")
    public String capital() {
        return model.chat("What is the capital of Madagascar?");
    }

    @GetMapping("/triage")
    public Map<String, Evaluation> chat() {
        Map<String, Evaluation> result = new HashMap<>();

        String claim = "I love the service you offer.";

        Evaluation triage = triageService.triage(claim);

        System.out.println(claim);
        System.out.println(triage);

        result.put(claim, triage);

        claim = """
                I couldn't resolve my problem,
                I need to wait for 2 hours to be attended and no solution yet,
                the service is horrible. I hate your bank.
                """;

        Evaluation triage2 = triageService.triage(claim);

        System.out.println(claim);
        System.out.println(triage2);

        result.put(claim, triage2);

        return result;
    }
}
