package main.java.lab1;

import java.util.*;

/**
 * Parking class simulates a parking lot with limited capacity.
 * Demonstrates correct thread synchronization using wait/notify.
 * Now supports persistent data saving/loading between runs.
 */
public class Parking {
    private final int capacity;
    private final Deque<ParkedCar> cars = new LinkedList<>();

    /** Inner class that keeps both Car and side info */
    public static class ParkedCar {
        private final Car car;
        private final String side; // "L" or "R"

        public ParkedCar(Car car, String side) {
            this.car = car;
            this.side = side;
        }

        public Car getCar() { return car; }
        public String getSide() { return side; }
    }

    public Parking(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException(" Parking capacity must be greater than zero!");
        this.capacity = capacity;

        // Load previous session data
        cars.addAll(ParkingDataManager.load());
        if (!cars.isEmpty()) {
            System.out.println("Nice! Loaded " + cars.size() + " cars from last session.");
        }
    }

    /** Parks a car. Waits if full. */
    public synchronized void park(Car car, boolean leftSide) throws InterruptedException {
        while (cars.size() >= capacity) {
            System.out.println("Warning! Parking is full. " + car + " is waiting...");
            wait();
        }

        String side = leftSide ? "L" : "R";
        ParkedCar parked = new ParkedCar(car, side);
        if (leftSide) cars.addFirst(parked);
        else cars.addLast(parked);

        System.out.println("That car " + car + " parked on the " + (leftSide ? "LEFT" : "RIGHT") + " side. Total: " + cars.size());
        ParkingDataManager.save(new ArrayList<>(cars));
        notifyAll();
    }

    /** Removes a car. Waits if empty. */
    public synchronized void leave() throws InterruptedException {
        while (cars.isEmpty()) {
            System.out.println("Wait. Parking is empty. Waiting for a car...");
            wait();
        }
        ParkedCar removed = cars.pollFirst();
        System.out.println(" That Car left: " + removed.getCar());
        System.out.println("Thank you, Good Bye !");

        ParkingDataManager.save(new ArrayList<>(cars));
        notifyAll();
    }

    /** Default park on left side */
    public synchronized void park(Car car) throws InterruptedException {
        park(car, true);
    }

    /** Shows parking state as a formatted table */
    public synchronized void displayStatus() {
        System.out.println("\n=== PARKING STATUS ===");
        System.out.printf("%-25s | %-15s | %-6s%n", "Car (Model + Number)", "Owner", "Side");
        System.out.println("-----------------------------------------------");

        if (cars.isEmpty()) {
            System.out.println("No cars currently parked.");
        } else {
            for (ParkedCar pc : cars) {
                System.out.printf("%-25s | %-15s | %-6s%n",
                        pc.getCar().getModel() + " " + pc.getCar().getNumber(),
                        pc.getCar().getOwner(),
                        pc.getSide().equals("L") ? "Left" : "Right");
            }
        }

        System.out.println("===============================================\n");
    }

    public int getCapacity() { return capacity; }
    public int getOccupied() { return cars.size(); }

    public static String extractModel(String carInfo) {
        String[] parts = carInfo.split(" ", 2);
        return parts.length > 0 ? parts[0] : "UnknownModel";
    }

    public static String extractNumber(String carInfo) {
        String[] parts = carInfo.split(" ", 2);
        return parts.length > 1 ? parts[1] : "UnknownNumber";
    }
}
