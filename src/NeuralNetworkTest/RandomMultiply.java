package NeuralNetworkTest;

import NeuralNetwork.*;

import java.io.IOException;
import java.util.*;

public class RandomMultiply {


    public static NeuralNetwork nn = new NeuralNetwork();
    public static InputNeuron[] inputs = new InputNeuron[2];
    public static WorkingNeuron[] outputs = new WorkingNeuron[1];
    public static List<Float> x1;
    public static List<Float> x2;
    public static List<Float> l1;


    public static void main(String[] args) throws IOException {


        for (int i = 0; i < 2; i++) {
            inputs[i] = nn.createNewInput();
        }

        for (int i = 0; i < 1; i++) {
            outputs[i] = nn.createNewOutput();
        }



        Random random = new Random();
        float[] weights = new float[2 * 1];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat();
        }
        nn.createFullMesh(weights);



        float epsilon = 0.0050f;
        x1 = new ArrayList<>();
        x2 = new ArrayList<>();
        l1 = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            x1.add(random.nextFloat()*100);
        }
        for (int i = 0; i < 10000; i++) {
            x2.add(random.nextFloat()*100);
        }
        for (int i = 0; i < 10000; i++) {
            l1.add(x1.get(i) * x2.get(i));
        }




        int w = 51;
        while (w < 50) {
            w++;


            test(); // Gibt das ergebnis des trainingsverhalten an...

            for (int i = 0; i < x1.size(); i++) {


                inputs[0].setValue(x1.get(i));
                inputs[1].setValue(x2.get(i));



                float[] schoulds = new float[1];
                schoulds[0] = l1.get(i);
             //   System.out.println(l1.get(i));
                nn.backpropagation(schoulds, epsilon,1);
            }

            epsilon *= 0.9f;
        }


        while (true){


            Scanner scanner = new Scanner(System.in);

            System.out.println("Geben Sie eine Zahl ein: ");
            int x = Integer.parseInt(scanner.nextLine());
            System.out.println("Geben Sie noch eine Zahl ein: ");
            int y = Integer.parseInt(scanner.nextLine());

            nn.reset();
            inputs[0].setValue(x);
            inputs[1].setValue(y);
            for (WorkingNeuron out : outputs){
                System.out.println(out.getValue());
            }

            float schoulds[] = new float[1];

            schoulds[0]= x * y;
            System.out.println(schoulds[0]);
            nn.backpropagation(schoulds, 0.01f,1);

        }

    }

    public static void test() {
        List<Float> t1 = new ArrayList<>();
        List<Float> t2 = new ArrayList<>();
        List<Float> lt1 = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 5000; i++) {
            t1.add(random.nextFloat()*10);
        }
        for (int i = 0; i < 5000; i++) {
            t2.add(random.nextFloat()*100);
        }
        for (int i = 0; i < 5000; i++) {
            lt1.add(t1.get(i) * t2.get(i));
        }


        int correct = 0;
        int wrong = 0;
        float[] prob = new float[lt1.size()];

        for (int i = 0; i < t1.size(); i++) {
            nn.reset();

            inputs[0].setValue(t1.get(i));
            inputs[1].setValue(t2.get(i));



            prob[i] = outputs[0].getValue();



            boolean wasCorrekt = false;


            if (Math.abs(lt1.get(i) -  prob[i]) < 0.01f) {
                wasCorrekt = true;
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


}
