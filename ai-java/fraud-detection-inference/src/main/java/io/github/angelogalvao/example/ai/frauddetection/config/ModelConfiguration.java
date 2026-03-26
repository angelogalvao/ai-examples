package io.github.angelogalvao.example.ai.frauddetection.config;

import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import io.github.angelogalvao.example.ai.frauddetection.model.TransactionDetails;
import io.github.angelogalvao.example.ai.frauddetection.model.TransactionTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class ModelConfiguration {

    private static final float THRESHOLD = 0.8f;

    @Bean
    public Criteria<TransactionDetails, Boolean> criteria() {
        String modelLocation = Thread.currentThread().getContextClassLoader().getResource("model.onnx").toExternalForm();

        return Criteria.builder()
                .setTypes(TransactionDetails.class, Boolean.class)
                .optModelUrls(modelLocation)
                .optTranslator(new TransactionTransformer(THRESHOLD))
                .optEngine("OnnxRuntime")
                .build();
    }

    @Bean
    public ZooModel<TransactionDetails, Boolean> model(@Qualifier("criteria") Criteria<TransactionDetails, Boolean> criteria) throws Exception {
        return criteria .loadModel();
    }

    @Bean
    public Supplier<Predictor<TransactionDetails, Boolean>> predictorProvider(ZooModel<TransactionDetails, Boolean> model) {
        return model::newPredictor;
    }
}
