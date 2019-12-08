package NeuralNetwork.ActivationFunctions;

import java.io.Serializable;

/**
 * Zwischen 0 und 1
 */
public class Sigmoid implements ActivationFunction, Serializable {
    @Override
    public float activation(float input) {

        return (float) (1 / (1 + Math.pow(Math.E, -input)));
    }

    @Override
    public float abgeleiteteActivation(float input) {
        float sigmoid = activation(input);
        return sigmoid * (1 - sigmoid);
    }
}
