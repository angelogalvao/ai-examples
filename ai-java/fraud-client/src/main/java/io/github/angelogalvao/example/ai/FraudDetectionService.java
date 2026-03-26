package io.github.angelogalvao.example.ai;

import io.github.angelogalvao.example.ai.model.FraudResponse;
import io.github.angelogalvao.example.ai.model.TransactionDetails;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/inference")
@RegisterRestClient(configKey = "fraud-model")
public interface FraudDetectionService {

    @POST
    FraudResponse isFraud(TransactionDetails transactionDetails);
}
