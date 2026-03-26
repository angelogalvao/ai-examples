package io.github.angelogalvao.example.ai.frauddetection.controller;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import io.github.angelogalvao.example.ai.frauddetection.model.FraudResponse;
import io.github.angelogalvao.example.ai.frauddetection.model.TransactionDetails;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@RestController
public class FraudDetectionInferenceController {

    @Resource
    Supplier<Predictor<TransactionDetails, Boolean>> predictorSupplier;

    private final WebClient webClient;

    public FraudDetectionInferenceController() {
        this.webClient = WebClient.create("http://localhost:8080");
    }

    @PostMapping("/inference")
    FraudResponse detectFraud(@RequestBody TransactionDetails transactionDetails) throws TranslateException {
        try (var p = predictorSupplier.get() ){
            boolean fraud = p.predict(transactionDetails);
            return new FraudResponse(transactionDetails.txId(), fraud);
        }
    }

    @GetMapping("/fraud/{txId}")
    FraudResponse detectFraud(@PathVariable String txId) {

        TransactionDetails transactionDetails = new TransactionDetails(txId, 0.3111400080477545f, 1.9459399775518593f, true, true, false);

        final ResponseEntity<FraudResponse> fraudResponseResponseEntity = webClient.post()
                .uri("/inference")
                .body(Mono.just(transactionDetails), TransactionDetails.class)
                .retrieve()
                .toEntity(FraudResponse.class)
                .block();

        return fraudResponseResponseEntity.getBody();
    }
}
