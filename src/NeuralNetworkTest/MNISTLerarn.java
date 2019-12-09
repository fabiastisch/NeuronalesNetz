package NeuralNetworkTest;

import NEU.Main;
import NeuralNetwork.*;


import java.io.IOException;
import java.util.*;

public class MNISTLerarn {

    public static List<MNISTDecoder.Digit> digits;
    public static List<MNISTDecoder.Digit> digitsTest;
    public static NeuralNetwork nn = new NeuralNetwork();
    public static InputNeuron[][] inputs = new InputNeuron[28][28];
    public static WorkingNeuron[] outputs = new WorkingNeuron[10];
    public static List<Connection> connections= new ArrayList();

    public static void main(String[] args) throws IOException {

        digits = MNISTDecoder.loadDataSet("src/DataSets/MNIST/train-images.idx3-ubyte", "src/DataSets/MNIST/train-labels.idx1-ubyte");
        digitsTest = MNISTDecoder.loadDataSet("src/DataSets/MNIST/t10k-images.idx3-ubyte", "src/DataSets/MNIST/t10k-labels.idx1-ubyte");


        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                inputs[i][j] = nn.createNewInput();
            }
        }

        for (int i = 0; i < 10; i++) {
            outputs[i] = nn.createNewOutput();
        }

        int hiddenN = 100;
        nn.createHiddenNeurons(hiddenN);


        Random random = new Random();
        float[] weights = new float[28 * 28 * hiddenN + hiddenN * 10];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat();
        }
        nn.createFullMesh(weights);
        connections = nn.setConnectionsFromFile();



        float epsilon = 0.0050f;


        int wh = 3;



        while (wh < 3) {
            System.out.println("Epoche: " + wh);
            wh++;
            test(); // Gibt das ergebnis des trainingsverhalten an...

            for (int i = 0; i < digits.size(); i++) {

                for (int j = 0; j < 28; j++) {
                    for (int y = 0; y < 28; y++) {

                        inputs[j][y].setValue(MNISTDecoder.toUnsignedByte(digits.get(i).data[j][y]) / 255f);
                    }
                }

                float[] schoulds = new float[10];
                schoulds[digits.get(i).label] = 1;
                nn.backpropagation(schoulds, epsilon,644);
            }

            epsilon *= 0.9f;

            nn.saveConnections();
        }



        Main.main(null);
        Main.setNN(nn);
    }

    public static void insertImage(byte[][] bytes) {

        for (int j = 0; j < 28; j++) {
            for (int y = 0; y < 28; y++) {

                inputs[j][y].setValue(MNISTDecoder.toUnsignedByte(bytes[j][y]) / 255f);
            }
        }



    }

    public static ProbabilityDigit[] insertdouble(double[] doubles) {
        int n = 0;
        for (int j = 0; j < 28; j++) {
            for (int y = 0; y < 28; y++) {
                float temp = (float) doubles[n++] / 255f;
                inputs[j][y].setValue(temp);
            }
        }
        ProbabilityDigit[] probDigits = new ProbabilityDigit[10];
        for (int j = 0; j < probDigits.length; j++) {
            probDigits[j] = new ProbabilityDigit(j, outputs[j].getValue());
        }

        Arrays.sort(probDigits, Collections.reverseOrder());
        return probDigits;


    }

    public static void test() {

        int correct = 0;
        int wrong = 0;

        for (int i = 0; i < digitsTest.size(); i++) {
            nn.reset();
            for (int j = 0; j < 28; j++) {
                for (int y = 0; y < 28; y++) {

                    inputs[j][y].setValue(MNISTDecoder.toUnsignedByte(digitsTest.get(i).data[j][y]) / 255f);
                }
            }
            ProbabilityDigit[] probabilityDigits = new ProbabilityDigit[10];
            for (int j = 0; j < probabilityDigits.length; j++) {
                probabilityDigits[j] = new ProbabilityDigit(j, outputs[j].getValue());
            }



            Arrays.sort(probabilityDigits, Collections.reverseOrder());

            boolean wasCorrekt = false;
            for (int j = 0; j < 1; j++) {
                if (digitsTest.get(i).label == probabilityDigits[j].DIGIT) {
                    wasCorrekt = true;
                }
            }

            if (wasCorrekt) {
                correct++;
            } else {
                wrong++;
            }

        }

        float percent = (float) correct / (float) (correct + wrong);
        System.out.println(percent);
    }

    public static void  learnNew(int a){
        float[] schoulds = new float[10];

        schoulds[a] = 1;
        System.out.println(schoulds[a-1]);

        nn.backpropagation(schoulds,1,1);
    }

    public static class ProbabilityDigit implements Comparable<ProbabilityDigit> {
        public final int DIGIT;
        public float probability;

        public ProbabilityDigit(int DIGIT, float probability) {
            this.DIGIT = DIGIT;
            this.probability = probability;
        }

        @Override
        public int compareTo(ProbabilityDigit o) {
            return Float.compare(probability, o.probability);
        }
    }
}












