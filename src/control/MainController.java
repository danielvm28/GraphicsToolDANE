package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.GraphicsData;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    private ArrayList<Double> canvasXValues;

    private ArrayList<Double> canvasYValues;

    private int[] numbersX;

    private int[] numbersY;

    public MainController() {
        canvasXValues = new ArrayList<>();
        canvasYValues = new ArrayList<>();
        numbersX = new int[3];
        numbersY = new int[3];
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();

        try {
            GraphicsData.readData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gets the max value for each dataset and calculates its fourth part
        double maxX = getMax(GraphicsData.xValues);
        double maxY = getMax(GraphicsData.yValues);
        double fourthPartX = (maxX / 4);
        double fourthPartY = (maxY / 4);

        numbersX[0] = (int) fourthPartX;
        numbersY[0] = (int) fourthPartY;

        // Fills an array with fourth parts to later paint the canvas
        for (int i = 1; i < 3; i++) {
            numbersX[i] = numbersX[i-1] + (int) fourthPartX;
            numbersY[i] = numbersY[i-1] + (int) fourthPartY;
        }

        // Calculates the relation constants to correctly paint the canvas with the given data
        double relationConstantX = (canvas.getWidth() / maxX) * 0.99;
        double relationConstantY = (canvas.getHeight() / maxY) * 0.99;

        // Adds the specific coordinates of the points to an ArrayList
        for (int i = 0; i < GraphicsData.yValues.size(); i++) {
            canvasYValues.add(Math.abs((GraphicsData.yValues.get(i) * relationConstantY) - canvas.getHeight()));
            canvasXValues.add(GraphicsData.xValues.get(i) * relationConstantX);
        }

        // Paints the canvas
        paint();
    }

    public void paint(){
        // Plot the points and lines

        gc.setFill(Color.rgb(240,240,240));
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLUE);
        for (int i = 0; i < canvasXValues.size(); i++) {
            gc.fillOval(canvasXValues.get(i), canvasYValues.get(i), 6, 6);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int i = 0; i < canvasXValues.size(); i++) {
            gc.lineTo(canvasXValues.get(i), canvasYValues.get(i));
        }

        gc.stroke();

        // ----------------------------------------------

        // Paint the grid

        gc.setLineWidth(0.6);
        gc.setStroke(Color.GRAY);

        // Horizontal lines
        for (int i = 1; i < 4; i++) {
            gc.strokeLine(0, (canvas.getHeight() / 4) * i, canvas.getWidth(), (canvas.getHeight() / 4) * i);
        }

        // Vertical lines
        for (int i = 1; i < 4; i++) {
            gc.strokeLine((canvas.getWidth() / 4) * i, 0, (canvas.getWidth() / 4) * i, canvas.getHeight());
        }

        gc.setFill(Color.BLACK);

        // X numbers
        for (int i = 1; i < 4; i++) {
            gc.fillText(numbersX[i-1] + "", (canvas.getWidth() / 4) * i, canvas.getHeight() - (canvas.getHeight() * 0.01));
        }

        // X label
        gc.fillText(GraphicsData.labels[0], canvas.getWidth() - 30, canvas.getHeight() - (canvas.getHeight() * 0.01));

        // Y numbers
        for (int i = 1, j = 2; i < 4; i++, j--) {
            gc.fillText(numbersY[j] + "", canvas.getWidth() * 0.01, ((canvas.getHeight() / 4) * i) - 5);
        }

        // Y label
        gc.fillText(GraphicsData.labels[1], canvas.getWidth() * 0.01,20);
    }

    private double getMax(ArrayList<Double> arr){
        ArrayList<Double> arr2 = new ArrayList<>();
        arr2.addAll(arr);
        Collections.sort(arr2);
        return arr2.get(arr2.size()-1);
    }
}
