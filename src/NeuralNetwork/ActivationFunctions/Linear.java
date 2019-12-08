package NeuralNetwork.ActivationFunctions;

//Identity
public class Linear implements ActivationFunction{


    @Override
    public float activation(float input) {
        return input;
    }

    @Override
    public float abgeleiteteActivation(float input) {
        return 1;
    }
}
