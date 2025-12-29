package org.example.clothing;

import java.time.LocalDate;

/**
 * Domain object representing a clothing item.
*/

public class Cloth {
    private final String name;
    private final String fabricType;
    private final String city;
    private final LocalDate productionDate;
    private final double price;

    public Cloth(String name, String fabricType, String city, LocalDate productionDate, double price) {
        this.name = name;
        this.fabricType = fabricType;
        this.city = city;
        this.productionDate = productionDate;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getFabricType() {
        return fabricType;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public double getPrice() {
        return price;
    }

    public int getMonthsSinceProduction() {
        return java.time.Period.between(productionDate, LocalDate.now()).getYears() * 12
                + java.time.Period.between(productionDate, LocalDate.now()).getMonths();
    }
}
