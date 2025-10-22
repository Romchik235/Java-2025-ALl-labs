package test.java.lab1;

import main.java.lab1.UnsafeParking;
import main.java.lab1.Parking;
import main.java.lab1.Car;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnsafeParkingTest {

    @Test
    public void testUnsafeParkingAllowsOverfill() {
        UnsafeParking unsafe = new UnsafeParking(2);
        unsafe.park(new Car("KIA AA001"));
        unsafe.park(new Car("Dodge BB002"));
        unsafe.park(new Car("Mazda CC003"));
        assertDoesNotThrow(unsafe::leave);
    }

    @Test
    public void testSafeParkingWaitsProperly() throws InterruptedException {
        Parking parking = new Parking(2);
        Thread t1 = new Thread(() -> {
            try {
                parking.park(new Car("KIA AA001"), true);
                parking.park(new Car("Dodje BB002"), false);
                parking.park(new Car("Mazda CC003"), true);
            } catch (InterruptedException e) {
                fail("Thread interrupted unexpectedly");
            }
        });

        t1.start();
        Thread.sleep(1000);
        assertTrue(t1.isAlive(), "Thread should be waiting for free space");

        parking.leave();
        Thread.sleep(1000);

        assertFalse(t1.isAlive(), "Thread should finish after space is freed");
    }
}
