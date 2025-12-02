package task6.task1.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Guest implements Serializable {
    private String name;
    private List<Service> services;
    private static int nextId = 1;
    private int id;

    public Guest(String name) {
        this.id = nextId++;
        this.name = name;
        this.services = new ArrayList<>();
    }

    public void addService(Service service) {
        services.add(service);
        System.out.println(name + " заказал услугу: " + service);
    }

    public double getTotalServicePrice() {
        return services.stream()
                .mapToDouble(Service::getPrice)
                .sum();
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

    @Override
    public String toString() {
        return "Гость: " + name + ", услуг на сумму " + getTotalServicePrice() + " руб.";
    }
}