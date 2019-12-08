package NeuralNetwork;

import NeuralNetwork.ActivationFunctions.*;
import NeuralNetwork.ActivationFunctions.Boolean;

import java.util.ArrayList;
import java.util.List;

public class WorkingNeuron extends Neuron{
    private List<Connection> connections = new ArrayList<>();
    private ActivationFunction activationFunktion = new Sigmoid();;
    private float smallDelta = 0;
    private float value = 0;
    private boolean valueClean = false;


    @Override
    public float getValue() {

        if (!valueClean) {
            float sum = 0;
            int i = 0;
            for (Connection c : connections) {
                sum += c.getValue();
            }


            value =  activationFunktion.activation(sum) ;

        }


        return value;

    }

    public void reset(){
        valueClean = false;
        smallDelta = 0;
    }

    public void addConnection(Connection c){
        connections.add(c);
    }


    public void deltaLearning(float epsilon) {
        float f = activationFunktion.abgeleiteteActivation(getValue()) * epsilon * smallDelta;

        for (int i = 0; i < connections.size(); i++){
            float bigDelta = f * connections.get(i).getNeuron().getValue();
            connections.get(i).addWeight(bigDelta);
        }
    }

    public void calculateOutputDelta(float should){
        smallDelta = should - getValue();
    }

    public void backpropagateSmallDelta(){
        for (Connection c : connections){
            Neuron n = c.getNeuron();
            if (n instanceof WorkingNeuron) {
                WorkingNeuron workingNeuron = (WorkingNeuron) n;
                workingNeuron.smallDelta += this.smallDelta * c.getWeight();
            }
        }
    }

    public void applyBatch(){
        for (Connection c : connections){
            c.applyBatch();
        }
    }

}
