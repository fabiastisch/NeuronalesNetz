package NeuralNetworkTest;

import Tools.ImageResizer;
import Tools.ImageUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DrawImage extends Application {

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        javafx.scene.control.Button button = new Button("Klick me");

        Canvas canvas = new Canvas(400, 400);
        root.getChildren().add(canvas);
        root.getChildren().add(button);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                WritableImage a = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setTransform(javafx.scene.transform.Transform.scale(1000,1000));
                a = canvas.snapshot(null,a);


                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(a, null), "png", new File("src/test.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

               /* int width = 28, height = 28;

                BufferedImage aa = SwingFXUtils.fromFXImage(a,null);
                aa = ImageResizer.resizeImage(aa,width,height);

                try {
                    ImageIO.write(aa, "png", new File("src/test.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
               /* try {
                    MNISTLerarn.insertImage(toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

               BufferedImage bufferedImage = SwingFXUtils.fromFXImage(a,null);

               // ImageUtils
               ImageUtils.scale(bufferedImage,20,20);
            //   ImageUtils.addImageToCenter(bufferedImage,28,28);
                

            }
        });



        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent event) {

                    }
                });

       // StackPane root = new StackPane();
       // root.getChildren().add(canvas);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initDraw(GraphicsContext gc){
       /* double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();*/

        gc.setLineWidth(10);

    }

    private static ImageView createScaledView(Node node, int scale) {
        final Bounds bounds = node.getLayoutBounds();

        final WritableImage image = new WritableImage(
                (int) Math.round(bounds.getWidth() * scale),
                (int) Math.round(bounds.getHeight() * scale));

        final SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        final ImageView view = new ImageView(node.snapshot(spa, image));
        view.setFitWidth(bounds.getWidth());
        view.setFitHeight(bounds.getHeight());



        return view;
    }

    public byte[][] toByteArray() throws IOException {
        String dirName="src/test.png";
        ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
        BufferedImage img=ImageIO.read(new File(dirName));
        ImageIO.write(img, "png", baos);
        baos.flush();

        String base64String= Base64.encode(baos.toByteArray());
        baos.close();

      //  byte[] bytearray = baos.toByteArray();
        Path p = Paths.get("src/test.png");
        byte[] bytearray = Files.readAllBytes(p);


        System.out.println(bytearray.length);
        byte [][]data = new byte[28][28];
        int d = 0;
        for (int i = 0; i < 28; i++) {
            for (int k = 0; k < 28; k++) {
                data[i][k] = bytearray[d++];
            }
        }


        return data;
    }


}
