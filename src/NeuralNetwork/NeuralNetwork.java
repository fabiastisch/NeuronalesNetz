package NeuralNetwork;

import java.io.*;
import java.util.*;

public class NeuralNetwork {

    private List<InputNeuron> inputNeurons = new ArrayList<>();
    private List<WorkingNeuron> hiddenNeurons = new ArrayList<>();
    private List<WorkingNeuron> outputNeurons = new ArrayList<>();
    private List<Connection> connections = new ArrayList();
    private int trainingSample = 0;


    public void backpropagation(float[] shoulds, float epsilon, int applyAfter) {


        if (shoulds.length != outputNeurons.size()) {
            throw new IllegalArgumentException();
        }

        reset();

        for (int i = 0; i < shoulds.length; i++) {
            outputNeurons.get(i).calculateOutputDelta(shoulds[i]);
        }


        if (hiddenNeurons.size() > 0) {
            for (int i = 0; i < shoulds.length; i++) {
                outputNeurons.get(i).backpropagateSmallDelta();
            }
        }

        for (int i = 0; i < shoulds.length; i++) {
            outputNeurons.get(i).deltaLearning(epsilon);
        }

        for (int i = 0; i < hiddenNeurons.size(); i++) {
            hiddenNeurons.get(i).deltaLearning(epsilon);
        }

        if (trainingSample % applyAfter == 0) {
            for (int i = 0; i < shoulds.length; i++) {
                outputNeurons.get(i).applyBatch();
            }

            for (int i = 0; i < hiddenNeurons.size(); i++) {
                hiddenNeurons.get(i).applyBatch();
            }
        }
        trainingSample++;

    }

    public void createFullMesh(float... weights) {
        if (hiddenNeurons.size() == 0) {
            if (weights.length != inputNeurons.size() * outputNeurons.size()) {
                throw new RuntimeException();
            }
            int index = 0;


            for (WorkingNeuron w : outputNeurons) {
                for (InputNeuron i : inputNeurons) {
                    Connection c = new Connection(i, weights[index++]);
                    connections.add(c);
                    w.addConnection(c);
                }
            }


        } else {
            if (weights.length != inputNeurons.size() * hiddenNeurons.size() + hiddenNeurons.size() * outputNeurons.size()) {
                throw new RuntimeException();
            }
            int index = 0;

            for (WorkingNeuron hidden : hiddenNeurons) { // Hidden schicht mit der Input schicht verbunden
                for (InputNeuron i : inputNeurons) {
                    Connection c = new Connection(i, weights[index++]);
                    connections.add(c);
                    hidden.addConnection(c);
                }
            }

            for (WorkingNeuron w : outputNeurons) { // Output schicht mit der Hidden schicht verbunden
                for (WorkingNeuron hidden : hiddenNeurons) {
                    Connection c = new Connection(hidden, weights[index++]);
                    connections.add(c);
                    w.addConnection(c);
                }
            }

        }


    }

    public void createFullMesh() {
        if (hiddenNeurons.size() == 0) {
            for (WorkingNeuron w : outputNeurons) {
                for (InputNeuron i : inputNeurons) {
                    w.addConnection(new Connection(i, 0));
                }
            }
        } else { // mit Hidden Neuronen

            for (WorkingNeuron w : outputNeurons) { // Output schicht mit der Hidden schicht verbunden
                for (WorkingNeuron hidden : hiddenNeurons) {
                    w.addConnection(new Connection(hidden, 0));
                }
            }

            for (WorkingNeuron hidden : hiddenNeurons) { // Hidden schicht mit der Input schicht verbunden
                for (InputNeuron i : inputNeurons) {
                    hidden.addConnection(new Connection(i, 0));
                }
            }
        }


    }

    public void reset() {
        for (WorkingNeuron wn : outputNeurons) {
            wn.reset();
        }
        for (WorkingNeuron wn : hiddenNeurons) {
            wn.reset();
        }
    }

    public void createHiddenNeurons(int amount) {
        if (amount < 0) {
            throw new RuntimeException();
        }
        for (int i = 0; i < amount; i++) {
            hiddenNeurons.add(new WorkingNeuron());
        }
    }

    public WorkingNeuron createNewOutput() {
        WorkingNeuron workingNeuron = new WorkingNeuron();
        outputNeurons.add(workingNeuron);
        return workingNeuron;
    }

    public InputNeuron createNewInput() {
        InputNeuron inputNeuron = new InputNeuron();
        inputNeurons.add(inputNeuron);
        return inputNeuron;
    }

    public void save() {
        String path = "src/save.sav";
        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {

            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(this);

            oos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveConnections() {
        System.out.println("Save");
        String path = "src/connections.sav";
        if (new File(path).exists()) {
            new File(path).delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(connections);

            oos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public List<Connection> setConnectionsFromFile() {
        String path = "src/connections.sav";

        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            List<Connection> c = (List<Connection>) ois.readObject();

            fis.available();
            fis.close();
            ois.close();

            ListIterator<Connection> iterator = c.listIterator();

            for (Connection connection : connections){
                connection.setWeight(iterator.next().getWeight());
            }

            /*if (hiddenNeurons.size() == 0) {
                for (WorkingNeuron w : outputNeurons) {
                    for (InputNeuron i : inputNeurons) {
                        w.addConnection(iterator.next());
                    }
                }
            } else { // mit Hidden Neuronen

                for (WorkingNeuron w : outputNeurons) { // Output schicht mit der Hidden schicht verbunden
                    for (WorkingNeuron hidden : hiddenNeurons) {
                        w.addConnection(iterator.next());
                    }
                }

                for (WorkingNeuron hidden : hiddenNeurons) { // Hidden schicht mit der Input schicht verbunden
                    for (InputNeuron i : inputNeurons) {
                        hidden.addConnection(iterator.next());
                    }
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return connections;
    }


}
