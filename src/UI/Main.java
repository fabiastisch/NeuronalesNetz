package UI;

import NeuralNetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static final int UI_SIZE = 600;
    public static NeuralNetwork nn;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            try {
                createFrame().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private static JFrame createFrame() throws IOException {
        final JFrame frame = new JFrame("Draw a number");
        frame.setSize(UI_SIZE, UI_SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final MainPanel mainUi = new MainPanel();
        frame.add(mainUi);

        return frame;
    }

    public static void setNN(NeuralNetwork nn) {
        nn = nn;
    }
}
