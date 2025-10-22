package main.java.lab1;

import java.io.*;
import java.util.*;

/**
 * Utility class for saving and loading parking data to/from a file.
 */
public class ParkingDataManager {
    private static final String FILE_PATH = "parking_data.txt";

    /** Save all cars (with owner and side) to file */
    public static synchronized void save(List<Parking.ParkedCar> cars) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Parking.ParkedCar pc : cars) {
                writer.println(pc.getCar().getModel() + " " + pc.getCar().getNumber() + ";" +
                        pc.getCar().getOwner() + ";" + pc.getSide());
            }
        } catch (IOException e) {
            System.err.println(" Error saving data: " + e.getMessage());
        }
    }

    /** Load cars from file (if exists) */
    public static synchronized List<Parking.ParkedCar> load() {
        List<Parking.ParkedCar> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String[] carParts = parts[0].split(" ", 2);
                    String model = carParts[0];
                    String number = (carParts.length > 1) ? carParts[1] : "UNKNOWN";
                    String owner = parts[1];
                    String side = parts[2];
                    list.add(new Parking.ParkedCar(new Car(model, number, owner), side));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return list;
    }
}
