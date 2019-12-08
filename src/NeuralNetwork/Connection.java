package NeuralNetwork;

import java.io.Serializable;


public class Connection implements Serializable {
    private static final long serialVersionUID = 8582433457601788991L;
    private Neuron neuron;
    private float weight;
    private float momentum = 0;
    private float weightAdd;

    public Connection(Neuron neuron, float weight) {
        this.neuron = neuron;
        this.weight = weight;

    }

    public float getValue(){
        return neuron.getValue() * weight;
    }

    public void applyBatch(){
        momentum += weightAdd;
        momentum *= 0.9f;
        weight += weightAdd + momentum;

        weightAdd = 0;
    }

    public void addWeight(float weightDelta) {
        weightAdd += weightDelta;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public float getWeight() {
        return weight;
    }
}
