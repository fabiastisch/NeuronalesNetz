package NeuralNetwork.ActivationFunctions;

public class HyperbolicTangent implements ActivationFunction {
    @Override
    public float activation(float input) {
        double ex = Math.pow(Math.E,input);
        double enx =  Math.pow(Math.E,-input);
        return (float) ((ex - enx) / (ex + enx));
    }

    @Override
    public float abgeleiteteActivation(float input) {
        float tamh = activation(input);
        return 1 - tamh * tamh;
    }
}
