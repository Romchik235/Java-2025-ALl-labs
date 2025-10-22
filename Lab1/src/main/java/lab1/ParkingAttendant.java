package main.java.lab1;

/**
 * Represents a parking attendant working with a shared Parking object.
 */
public class ParkingAttendant implements Runnable {
    private final Parking parking;
    private final boolean isParking;
    private final Car car;

    public ParkingAttendant(Parking parking, boolean isParking, Car car) {
        this.parking = parking;
        this.isParking = isParking;
        this.car = car;
    }

    @Override
    public void run() {
        try {
            if (isParking) {
                parking.park(car);
            } else {
                parking.leave();
            }
        } catch (InterruptedException e) {
            System.err.println("Error. Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
