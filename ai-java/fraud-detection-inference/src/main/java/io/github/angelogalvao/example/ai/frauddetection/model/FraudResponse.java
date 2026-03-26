package io.github.angelogalvao.example.ai.frauddetection.model;

/**
 * This record represent the model response.
 * @param txId
 * @param fraud
 */
public record FraudResponse(String txId, boolean fraud) {
}
