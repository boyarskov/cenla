package task3.task2;

import java.util.ArrayList;
import java.util.List;

public class Bouquet {
    private List<Flower> flowers;

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public Bouquet() {
        flowers = new ArrayList<>();
    }

    public int calculateTotalPrice() {
        int total = 0;
        for (Flower flower : flowers) {
            total += flower.getPrice();
        }
        return total;
    }

    public void showBouquet() {
        if (flowers.isEmpty()) {
            System.out.println("There are no flowers in the bouquet");
        } else {
            System.out.println("Flowers in a bouquet::");
            for (Flower flower : flowers) {
                System.out.println(flower.getName() + " — " + flower.getPrice() + " rubles");
            }
        }
        System.out.println("Total bouquet price: " + calculateTotalPrice() + " rubles");
    }
}