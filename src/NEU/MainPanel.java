package NEU;


import NeuralNetwork.NeuralNetwork;
import NeuralNetworkTest.MNISTLerarn;
import Tools.ImageUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;


public class MainPanel extends JPanel implements NumberDrawnListener {


    private final JPanel drawPanelContainer;
    private final DrawPanel drawPanel;
    private final JLabel finalImage;
    private final JTextArea resultText;

    private final JTextField inputText;
    private final JButton inputButton;

    public MainPanel()  {
        // Init fields

        drawPanel = new DrawPanel(this);

        drawPanelContainer = new JPanel();
        inputButton = new JButton("Learn");
        inputText = new JTextField();
        finalImage = new JLabel();
        resultText = new JTextArea();

        // Set image labels

        finalImage.setText("Final input image");

        // Set JLabel text positions

        finalImage.setVerticalTextPosition(SwingConstants.BOTTOM);


        finalImage.setHorizontalTextPosition(SwingConstants.CENTER);


        drawPanelContainer.setBackground(Color.LIGHT_GRAY);

        // Layout panels
        final JPanel rightSideContainer = new JPanel();
        rightSideContainer.setLayout(new BoxLayout(rightSideContainer, BoxLayout.Y_AXIS));

        final JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setComponentSizes();


        rightSideContainer.add(finalImage);
        rightSideContainer.add(inputButton);
        rightSideContainer.add(inputText);

        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = inputText.getText();;
                int i = Integer.parseInt(s);

                MNISTLerarn.learnNew(i);

            }
        });



        drawPanelContainer.add(drawPanel);
        drawPanelContainer.add(resultText);



        topContainer.add(drawPanelContainer);
        topContainer.add(rightSideContainer);
        this.add(topContainer);


    }

    private void setComponentSizes() {
        final int half = Main.UI_SIZE / 2;
        final int leftSizeComponentHeight = Main.UI_SIZE;
        final int rightSideComponentHeight = Main.UI_SIZE / 3;

        final Dimension rightSideComponentSize = new Dimension(half, rightSideComponentHeight);
        drawPanelContainer.setPreferredSize(new Dimension(half, leftSizeComponentHeight));
        drawPanel.setPreferredSize(new Dimension(half, half));


        finalImage.setPreferredSize(rightSideComponentSize);
        resultText.setPreferredSize(new Dimension(half, 250));

        // Apparently JLabel needs maximum size set also or the icon does
        // not align correctly. I don't understand Swing...

        finalImage.setMaximumSize(rightSideComponentSize);

        finalImage.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void drawingFinished(final BufferedImage image) {

        final Rectangle bounds = ImageUtils.findBoundsOfBlackShape(image);
        final Dimension newDim = getScaledMnistDigitDimensions(bounds);

        final BufferedImage ausgeschnittenesImage = image.getSubimage(bounds.x,
                bounds.y,
                bounds.width,
                bounds.height);

        final BufferedImage scaled = ImageUtils.scale(ausgeschnittenesImage, newDim.width, newDim.height);


        final BufferedImage mnistInputImage
                = ImageUtils.addImageToCenter(scaled,
                ImageUtils.MNIST_IMAGE_SIZE,
                ImageUtils.MNIST_IMAGE_SIZE);



        finalImage.setIcon(new ImageIcon(mnistInputImage));
/*//TODO:
        final int result = digitDetector.recognize(mnistInputImage);
        resultText.setText("You wrote: " + result);*/

        double[] doubles= ImageUtils.getPixelsAndConvertToBlackAndWhite(mnistInputImage);
        MNISTLerarn.ProbabilityDigit[] probabilityDigits = MNISTLerarn.insertdouble(doubles);
        String s = "";
        for (MNISTLerarn.ProbabilityDigit p : probabilityDigits){
            s +=  "  " + p.DIGIT + ": " + p.probability*100 + "\n";
        }
        resultText.setText(s);


    }

    // Shamelessly copied from
    // http://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
    private static Dimension getScaledMnistDigitDimensions(final Rectangle bounds) {

        final int originalWidth = bounds.width;
        final int originalHeight = bounds.height;
        final int boundWidth = ImageUtils.MNIST_DIGIT_BOUNDS_SIZE;
        final int boundHeight = ImageUtils.MNIST_DIGIT_BOUNDS_SIZE;
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        // first check if we need to scale width
        if (originalWidth > boundWidth) {
            //scale width to fit
            newWidth = boundWidth;
            //scale height to maintain aspect ratio
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        // then check if we need to scale even with the new height
        if (newHeight > boundHeight) {
            //scale height to fit instead
            newHeight = boundHeight;
            //scale width to maintain aspect ratio
            newWidth = (newHeight * originalWidth) / originalHeight;
        }

        return new Dimension(newWidth, newHeight);
    }
}
