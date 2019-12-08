package NeuralNetwork.ActivationFunctions;

public interface ActivationFunction {
    public static Boolean ACTIVATIONBOOLEAN = new Boolean();
    public static Linear ACTIVATIONLINEAR = new Linear();
    public static Sigmoid ACTIVATIONSIGMOID = new Sigmoid();
    public static HyperbolicTangent ACTIVATIONHYPERBOLIC_TANGENT = new HyperbolicTangent();



    public float activation(float input);
    public float abgeleiteteActivation(float input);
}
