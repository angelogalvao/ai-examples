package io.github.angelogalvao.example.ai.frauddetection.model;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;

public class TransactionTransformer implements NoBatchifyTranslator<TransactionDetails, Boolean> {

    private final float threshold;

    public TransactionTransformer(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public Boolean processOutput(TranslatorContext translatorContext, NDList list) throws Exception {

        NDArray result = list.getFirst();
        float prediction = result.toFloatArray()[0];

        System.out.println("prediction: " + prediction);

        return prediction > threshold;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, TransactionDetails input) throws Exception {

        NDArray array = ctx.getNDManager().create(toFloatRepresentation(input), new Shape(1, 5));
        return new NDList(array);
    }

    private static float[] toFloatRepresentation(TransactionDetails td) {
        return new float[]{
                td.distanceFromLastTransaction(),
                td.ratioToMedianPrice(),
                booleanAsFloat(td.usedChip()),
                booleanAsFloat(td.usedPinNumber()),
                booleanAsFloat(td.onlineOrder())
        };
    }

    private static float booleanAsFloat(boolean flag) {
        return flag ? 1f : 0f;
    }
}
