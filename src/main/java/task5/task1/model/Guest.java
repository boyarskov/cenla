package task5.task1.model;

import java.util.ArrayList;
import java.util.List;

public class Guest {
    private String name;
    private List<Service> services;

    public Guest(String name) {
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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Гость: " + name + ", услуг на сумму " + getTotalServicePrice() + " руб.";
    }
}