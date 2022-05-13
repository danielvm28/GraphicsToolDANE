package model;

import java.io.*;
import java.util.ArrayList;

public class GraphicsData {
    public static ArrayList<Double> xValues = new ArrayList<>();
    public static ArrayList<Double> yValues = new ArrayList<>();
    public static String[] labels = new String[2];

    public static void readData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/data.csv")));

        // Read labels first
        String line = br.readLine();
        labels = line.split("[,;]");

        // Read all remaining data
        while ((line = br.readLine()) != null) {
            String[] values = line.split("[,;]");
            xValues.add(Double.parseDouble(values[0]));
            yValues.add(Double.parseDouble(values[1]));
        }
    }
}
