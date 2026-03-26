package io.github.angelogalvao.example.ai.frauddetection.controller;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import io.github.angelogalvao.example.ai.frauddetection.model.FraudDetectionGrpc;
import io.github.angelogalvao.example.ai.frauddetection.model.FraudRes;
import io.github.angelogalvao.example.ai.frauddetection.model.TransactionDetails;
import io.github.angelogalvao.example.ai.frauddetection.model.TxDetails;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Resource;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.function.Supplier;

@GrpcService
public class FraudDetectionInferenceGrpcController extends FraudDetectionGrpc.FraudDetectionImplBase {

    @Resource
    Supplier<Predictor<TransactionDetails, Boolean>> predictorSupplier;

    @Override
    public void predict(TxDetails request, StreamObserver<FraudRes> responseObserver) {
        TransactionDetails td = new TransactionDetails(
                request.getTxId(),
                request.getDistanceFromLastTransaction(),
                request.getRatioToMedianPrice(),
                request.getUsedChip(),
                request.getUsedPinNumber(),
                request.getOnlineOrder()
        );

        try(var p = predictorSupplier.get()) {
            boolean fraud = p.predict(td);

            FraudRes fraudResponse = FraudRes.newBuilder().setTxId(td.txId()).setFraud(fraud).build();
        } catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }
}
