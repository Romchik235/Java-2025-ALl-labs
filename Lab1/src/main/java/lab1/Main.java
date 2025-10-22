package main.java.lab1;

import java.io.*;
import java.util.*;

public class Main {

    private static final String DATA_FILE = "parking_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== PARKING SIMULATION =====");

        // 1 - Фіксована місткість
        int capacity = 10;
        Parking parking = new Parking(capacity);
        System.out.println("Parking capacity is fixed: " + capacity + " spots available.");

        // Дані реєстрації
        Set<String> registeredCars = new HashSet<>();
        Map<String, String> carOwners = new HashMap<>();

        // 5 - Завантаження попередніх даних
        loadData(registeredCars, carOwners);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1 - Register new owner and your car");
            System.out.println("2 - Find your (my) car in the parking lot");
            System.out.println("3 - Remove a car");
            System.out.println("4 - Show parking status");
            System.out.println("5 - Demonstrate BAD synchronization");
            System.out.println("0 - Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (registeredCars.size() >= capacity) {
                        System.out.println("Attention ! Cannot register more cars. Parking capacity reached.");
                        break;
                    }
                    System.out.print("\nEnter owner's name: ");
                    String owner = scanner.nextLine().trim();

                    System.out.print("\nEnter car model and number (e.g., Toyota AB123, Mitsubishi AA001, Renault BB002): ");
                    String carInfo = scanner.nextLine().trim();

                    // 3 - Перевірка формату номера
                    if (!carInfo.matches("^[A-Za-z]+\\s[A-Z]{2}\\d{3}$")) {
                        System.out.println("Error ! Invalid format! Use format: 'Model AA123' (letters + 2 letters + 3 digits)");
                        break;
                    }

                    // 4 - Перевірка унікальності
                    if (registeredCars.contains(carInfo)) {
                        System.out.println("Error ! This car is already registered!");
                        break;
                    }

                    registeredCars.add(carInfo);
                    carOwners.put(carInfo, owner);
                    System.out.println("That Car " + carInfo + " successfully registered for " + owner);
                    break;

                case "2":
                    System.out.print("Enter your car model and number: ");
                    String carToPark = scanner.nextLine().trim();

                    if (!registeredCars.contains(carToPark)) {
                        System.out.println("Error! Car not registered. Please register first or contact administration.");
                        break;
                    }

                    System.out.print("Choose side (L - left / R - right): ");
                    String side = scanner.nextLine().trim().toUpperCase();
                    boolean left = side.equals("L");

                    new Thread(() -> {
                        try {
                            parking.park(new Car(
                                    Parking.extractModel(carToPark),
                                    Parking.extractNumber(carToPark),
                                    carOwners.get(carToPark)
                            ), left);
                        } catch (InterruptedException e) {
                            System.err.println("Thread error: " + e.getMessage());
                        }
                    }).start();
                    break;

                case "3":
                    new Thread(() -> {
                        try {
                            parking.leave();
                        } catch (InterruptedException e) {
                            System.err.println("Thread error: " + e.getMessage());
                        }
                    }).start();
                    break;

                case "4":
                    parking.displayStatus();
                    break;

                case "5":
                    runBadSynchronizationDemo();
                    break;

                case "0":
                    // 2 - Повідомлення при виході + збереження
                    System.out.println("Well. Saving data...");
                    saveData(registeredCars, carOwners);
                    System.out.println("Thank you for choosing us! Visit again. Goodbye!");
                    return;

                default:
                    System.out.println("Error ! Invalid option. Try again.");
            }
        }
    }

    /**
     * BAD synchronization demo
     */
    private static void runBadSynchronizationDemo() {
        System.out.println("=== BAD SYNCHRONIZATION DEMO ===");
        UnsafeParking unsafe = new UnsafeParking(2);

        Thread t1 = new Thread(() -> unsafe.park(new Car("Audi", "A1", "Alex")));
        Thread t2 = new Thread(() -> unsafe.park(new Car("BMW", "B2", "Bob")));
        Thread t3 = new Thread(unsafe::leave);
        Thread t4 = new Thread(() -> unsafe.park(new Car("Honda", "C3", "Charlie")));
        Thread t5 = new Thread(unsafe::leave);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            System.err.println("Demo interrupted: " + e.getMessage());
        }
    }

    /**
     * Save registered cars
     */
    private static void saveData(Set<String> cars, Map<String, String> owners) {
        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (String car : cars) {
                out.println(car + ";" + owners.get(car));
            }
            System.out.println("Nice ! Data saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Load previous data
     */
    private static void loadData(Set<String> cars, Map<String, String> owners) {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("(Data. No previous data found.)");
            return;
        }

        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                String[] parts = in.nextLine().split(";");
                if (parts.length == 2) {
                    cars.add(parts[0]);
                    owners.put(parts[0], parts[1]);
                }
            }
            System.out.println("Loaded " + cars.size() + " registered cars from file.");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}

//package main.java.lab1;
//
//import java.util.*;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("===== PARKING SIMULATION =====");
//        System.out.print("Enter parking capacity (integer > 0): ");
//
//        int capacity = 0;
//        try {
//            capacity = Integer.parseInt(scanner.nextLine());
//            if (capacity <= 0) throw new NumberFormatException();
//        } catch (NumberFormatException e) {
//            System.err.println("Error. Invalid number! Capacity must be positive.");
//            return;
//        }
//
//        Parking parking = new Parking(capacity);
//        Set<String> registeredCars = new HashSet<>();
//        Map<String, String> carOwners = new HashMap<>();
//
//        parking.displayStatus();
//
//        while (true) {
//            System.out.println("\nChoose an option:");
//            System.out.println("1 - Register new owner and his car");
//            System.out.println("2 - Find in Park a my car");
//            System.out.println("3 - Remove a car");
//            System.out.println("4 - Show parking status");
//            System.out.println("5 - Demonstrate BAD synchronization");
//            System.out.println("0 - Exit");
//            System.out.print("Your choice: ");
//
//            String choice = scanner.nextLine().trim();
//
//            switch (choice) {
//                case "1":
//                    if (registeredCars.size() >= parking.getCapacity()) {
//                        System.out.println("Attention! Cannot register more cars. Parking capacity reached.");
//                        break;
//                    }
//                    System.out.print("Enter owner's name: ");
//                    String owner = scanner.nextLine().trim();
//                    System.out.print("Enter car model and number (e.g., Toyota AB123): ");
//                    String carInfo = scanner.nextLine().trim();
//                    if (carInfo.isEmpty()) {
//                        System.out.println("Error! Invalid input!");
//                        break;
//                    }
//                    registeredCars.add(carInfo);
//                    carOwners.put(carInfo, owner);
//                    System.out.println("That Car " + carInfo + " registered for " + owner);
//                    break;
//
//                case "2":
//                    System.out.print("Enter your car model and number: ");
//                    String carToPark = scanner.nextLine().trim();
//                    if (!registeredCars.contains(carToPark)) {
//                        System.out.println("Error! Car not registered. Please register first.");
//                        break;
//                    }
//                    System.out.print("Choose side (L - left / R - right): ");
//                    String side = scanner.nextLine().trim().toUpperCase();
//                    boolean left = side.equals("L");
//                    new Thread(() -> {
//                        try {
//                            parking.park(
//                                    new Car(
//                                            Parking.extractModel(carToPark),
//                                            Parking.extractNumber(carToPark),
//                                            carOwners.get(carToPark)
//                                    ), left
//                            );
//                        } catch (InterruptedException e) {
//                            System.err.println("Thread error: " + e.getMessage());
//                        }
//                    }).start();
//                    break;
//
//                case "3":
//                    new Thread(() -> {
//                        try {
//                            parking.leave();
//                        } catch (InterruptedException e) {
//                            System.err.println("Thread error: " + e.getMessage());
//                        }
//                    }).start();
//                    break;
//
//                case "4":
//                    parking.displayStatus();
//                    break;
//
//                case "5":
//                    runBadSynchronizationDemo();
//                    break;
//
//                case "0":
//                    System.out.println("Thank you ! Goodbye! Parking data saved.");
//                    return;
//
//                default:
//                    System.out.println("Error! Invalid option. Try again.");
//            }
//        }
//    }
//
//    private static void runBadSynchronizationDemo() {
//        System.out.println("=== BAD SYNCHRONIZATION DEMO ===");
//        UnsafeParking unsafe = new UnsafeParking(2);
//        Thread t1 = new Thread(() -> unsafe.park(new Car("Audi A1", "Alex")));
//        Thread t2 = new Thread(() -> unsafe.park(new Car("BMW B2", "Bob")));
//        Thread t3 = new Thread(unsafe::leave);
//        Thread t4 = new Thread(() -> unsafe.park(new Car("Honda C3", "Charlie")));
//        Thread t5 = new Thread(unsafe::leave);
//        t1.start(); t2.start(); t3.start(); t4.start(); t5.start();
//        try {
//            t1.join(); t2.join(); t3.join(); t4.join(); t5.join();
//        } catch (InterruptedException e) {
//            System.err.println("Demo interrupted: " + e.getMessage());
//        }
//    }
//}
