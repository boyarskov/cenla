package task3.task2;

public class FlowerShopDemo {
    public static void main(String[] args) {
        Flower rose = new Rose();
        Flower tulip = new Tulip();
        Flower carnation = new Carnation();

        Bouquet bouquet = new Bouquet();

        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.addFlower(carnation);

        bouquet.showBouquet();
    }
}
