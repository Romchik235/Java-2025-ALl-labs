package main.java.lab1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Demonstrates unsafe access to shared resources without synchronization.
 * This version intentionally lacks synchronized, wait, and notify.
 */
public class UnsafeParking {
    private final int capacity;
    private final Queue<Car> cars = new LinkedList<>();

    public UnsafeParking(int capacity) {
        this.capacity = capacity;
    }

    public void park(Car car) {
        if (cars.size() >= capacity) {
            System.out.println("Error: Parking full (but thread ignores it): " + car);
        } else {
            cars.add(car);
            System.out.println("Attention !!! UNSAFE park: " + car + " parked. Occupied: " + cars.size());
        }
    }

    public void leave() {
        if (cars.isEmpty()) {
            System.out.println("UNSAFE remove: tried to remove car from EMPTY parking!");
        } else {
            Car car = cars.poll();
            System.out.println("UNSAFE leave: " + car + " left. Remaining: " + cars.size());
        }
    }
}
