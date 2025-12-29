package org.example.clothing;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Generator for creating random Cloth instances.
*/

public final class ClothingGenerator {
    private static final Random R = new Random();

    private static final List<String> NAMES = List.of("T-Shirt", "Jeans", "Hoodie", "Dress", "Skirt", "Jacket");
    private static final List<String> CITIES = List.of("Kyiv", "Lviv", "Odesa", "Dnipro", "Kharkiv");
    private static final List<String> FABRICS = List.of("Cotton", "Polyester", "Wool", "Linen", "Silk");

    public static Supplier<Cloth> generator() {
        return () -> {
            String name = NAMES.get(R.nextInt(NAMES.size()));
            String city = CITIES.get(R.nextInt(CITIES.size()));
            String fabric = FABRICS.get(R.nextInt(FABRICS.size()));
            LocalDate prodDate = LocalDate.now().minusMonths(R.nextInt(60));
            double price = Math.round((20 + R.nextDouble() * 200) * 100.0) / 100.0;
            return new Cloth(name, fabric, city, prodDate, price);
        };
    }
}
