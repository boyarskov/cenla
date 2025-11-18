package task6.task1.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private List<Room> rooms;
    private List<Service> services;

    public Hotel() {
        this.rooms = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Добавлен номер: " + room);
    }

    public void addService(Service service) {
        services.add(service);
        System.out.println("Добавлена услуга: " + service);
    }

    public Room getRoomByNumber(int number) {
        return rooms.stream()
                .filter(r -> r.getNumber() == number)
                .findFirst()
                .orElse(null);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Service> getServices() {
        return services;
    }
}
