package io.github.angelogalvao.example.ai;

import io.github.angelogalvao.example.ai.frauddetection.model.FraudDetection;
import io.github.angelogalvao.example.ai.frauddetection.model.FraudRes;
import io.github.angelogalvao.example.ai.frauddetection.model.TxDetails;
import io.github.angelogalvao.example.ai.model.FraudResponse;
import io.github.angelogalvao.example.ai.model.TransactionDetails;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.HashMap;
import java.util.Map;

@Path("/fraud")
public class TransactionResource {

    @RestClient
    FraudDetectionService fraudDetectionService;

    @GrpcClient("fraud")
    FraudDetection fraud;

    static Map<String, TransactionDetails> txs = new HashMap<>();
    static Map<String, Boolean> fraudTxs = new HashMap<>();

    @Startup
    public void populateData() {
        txs.put("1234", new TransactionDetails("1234",
                0.3111400080477545f,
                1.9459399775518593f,
                true, true, false));

        txs.put("5678", new TransactionDetails("5678",
                0.3111400080477545f,
                1.9459399775518593f,
                true, false, false));
    }

    @GET
    @Path("/{txId}")
    public FraudResponse detectFraud(@PathParam("txId") String txId) {
        final TransactionDetails transaction = findTransactionById(txId);

        final FraudResponse fraudResponse = fraudDetectionService.isFraud(transaction);

        markTransactionFraud(fraudResponse.txId(), fraudResponse.fraud());

        return fraudResponse;
    }

    @GET
    @Path("/grpc/{txId}")
    public Uni<FraudResponse> detectFraudGrpcClient(@PathParam("txId") String txId) {

        final TransactionDetails tx = findTransactionById(txId);

        final TxDetails txDetails = TxDetails.newBuilder()
                .setTxId(txId)
                .setDistanceFromLastTransaction(tx.distanceFromLastTransaction())
                .setRatioToMedianPrice(tx.ratioToMedianPrice())
                .setOnlineOrder(tx.onlineOrder())
                .setUsedChip(tx.usedChip())
                .setUsedPinNumber(tx.usedPinNumber())
                .build();

        final Uni<FraudRes> predicted = fraud.predict(txDetails);
        return predicted
                .onItem()
                .transform(fr -> new FraudResponse(fr.getTxId(), fr.getFraud()));
    }

    private TransactionDetails findTransactionById(String id) {
        return txs.get(id);
    }

    private void markTransactionFraud(String id, boolean isFraud) {
        fraudTxs.put(id, isFraud);
    }

    private boolean isFraudulent(String id) {
        return fraudTxs.get(id);
    }
}
