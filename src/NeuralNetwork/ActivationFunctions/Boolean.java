package NeuralNetwork.ActivationFunctions;

public class Boolean implements ActivationFunction {
    @Override
    public float activation(float input) {
        return input < 0 ? 0 : 1;
    }

    @Override
    public float abgeleiteteActivation(float input) {
        return 1;
    }


}
