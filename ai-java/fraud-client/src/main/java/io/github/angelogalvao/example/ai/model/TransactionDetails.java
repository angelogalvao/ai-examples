package io.github.angelogalvao.example.ai.model;

public record TransactionDetails(
        String txId,
        float distanceFromLastTransaction,
        float ratioToMedianPrice,
        boolean usedChip,
        boolean usedPinNumber,
        boolean onlineOrder
) {}