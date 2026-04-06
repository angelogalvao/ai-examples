package io.github.angelogalvao.example.ai;

import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/distance")
public class VectorResource {

    @Inject
    Predictor<String, float[]> predict;

    @GET
    @Path("/djl")
    @Produces(MediaType.TEXT_PLAIN)
    public String cosineDistance() throws TranslateException {

        float[] carVector    = predict.predict("car");
        float[] catVector    = predict.predict("cat");
        float[] kittenVector = predict.predict("kitten");

        StringBuilder distances = createStringRepresentationOfDistances(carVector, catVector, kittenVector);
        return distances.toString();
    }

    private static StringBuilder createStringRepresentationOfDistances(float[] carVector, float[] catVector, float[] kittenVector) {
        StringBuilder distances = new StringBuilder();

        distances.append("Car -> Cat: ");
        distances.append(cosineSimilarity(carVector, catVector));
        distances.append(System.lineSeparator());

        distances.append("Car -> Kitten: ");
        distances.append(cosineSimilarity(carVector, kittenVector));
        distances.append(System.lineSeparator());

        distances.append("Cat -> Kitten: ");
        distances.append(cosineSimilarity(catVector, kittenVector));
        distances.append(System.lineSeparator());
        return distances;
    }
    public static double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
