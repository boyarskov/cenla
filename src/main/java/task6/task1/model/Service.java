package task6.task1.model;

public class Service {
    private String name;
    private double price;
    private static int nextId = 1;
    private int id;

    public Service(String name, double price) {
        this.id = nextId++;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " (" + price + " руб)";
    }
}
