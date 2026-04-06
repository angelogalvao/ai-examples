package io.github.angelogalvao.example.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.output.Response;
import smile.manifold.TSNE;
import smile.plot.swing.Canvas;
import smile.plot.swing.ScatterPlot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class EmbeddingTextRepresentationMain {

    void main() throws InterruptedException, InvocationTargetException {

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        Response<Embedding> responseCar    = embeddingModel.embed("car");
        Response<Embedding> responseCat    = embeddingModel.embed("cat");
        Response<Embedding> responseKitten = embeddingModel.embed("kitten");

        float[] carVector = responseCar.content().vector();
        float[] catVector = responseCat.content().vector();
        float[] kittenVector = responseKitten.content().vector();

        StringBuilder distances = createStringRepresentationOfDistances(carVector, catVector, kittenVector);

        System.out.println("Vector distances: ");
        System.out.println(distances);

        List<float[]> points = List.of(carVector, catVector, kittenVector);
        double[][] pointsToReduce = toDoubleArray(points);

        TSNE tsne = new TSNE( pointsToReduce, 3 );
        double[][] reducedData = tsne.coordinates;

        Canvas canvas = ScatterPlot.of(reducedData).canvas();
        canvas.window();
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

    private static double[][] toDoubleArray(List<float[]> points) {
        double[][] pointsToReduce = points.stream()
                .map(floatArray -> {
                    double[] doubleArray = new double[floatArray.length];
                    for (int i = 0; i < floatArray.length; i++) {
                        doubleArray[i] = (double) floatArray[i];
                    }
                    return doubleArray;
                })
                .toArray(double[][]::new);
        return pointsToReduce;
    }

}
