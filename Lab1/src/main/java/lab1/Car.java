package main.java.lab1;

/**
 * Represents a car registered in the parking system.
 */
public class Car {
    private final String owner;
    private final String model;
    private final String number;

    /** Constructor for tests or quick creation (e.g. "KIA AA001") */
    public Car(String carInfo) {
        String[] parts = carInfo.split(" ", 2);
        if (parts.length == 2) {
            this.model = parts[0];
            this.number = parts[1];
        } else {
            this.model = carInfo;
            this.number = "UNKNOWN";
        }
        this.owner = "UnknownOwner";
    }

    /** Constructor for main program when model and owner are known */
    public Car(String model, String owner) {
        this.model = model;
        this.number = "UNKNOWN";
        this.owner = owner;
    }

    /** Full constructor for full car registration */
    public Car(String model, String number, String owner) {
        this.model = model;
        this.number = number;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getModel() {
        return model;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return model + " [" + number + "] (Owner: " + owner + ")";
    }
}
