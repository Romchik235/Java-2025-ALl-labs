package test.java.lab1;

import main.java.lab1.Car;
import main.java.lab1.Parking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingTest {
    @Test
    public void testParkingDoesNotExceedCapacity() throws InterruptedException {
        Parking parking = new Parking(2);
        parking.park(new Car("Mitsubishi AA111"));
        parking.park(new Car("Renault BB222"));

        Thread t = new Thread(() -> {
            try {
                parking.park(new Car("Volkswagen CC333"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t.start();
        Thread.sleep(500);
        assertTrue(t.isAlive(), "Thread should wait when parking is full");
        parking.leave();
        Thread.sleep(700);
        assertFalse(t.isAlive(), "Thread should finish after a spot frees up");
    }
}
