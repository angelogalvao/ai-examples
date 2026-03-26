package io.github.angelogalvao.example.ai.frauddetection.model;

/**
 * This record represent the input to the model to detect fraud transactions.
 * @param txId
 * @param distanceFromLastTransaction
 * @param ratioToMedianPrice
 * @param usedChip
 * @param usedPinNumber
 * @param onlineOrder
 */
public record TransactionDetails(
        String txId,
        float distanceFromLastTransaction,
        float ratioToMedianPrice,
        boolean usedChip,
        boolean usedPinNumber,
        boolean onlineOrder
        ) {
}
