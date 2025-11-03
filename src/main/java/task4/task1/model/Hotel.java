package task4.task1.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private List<Room> rooms;
    private List<Service> services;

    public Hotel() {
        this.rooms = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    // Добавление номера
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Добавлен номер: " + room);
    }

    // Добавление услуги
    public void addService(Service service) {
        services.add(service);
        System.out.println("Добавлена услуга: " + service);
    }

    // Поиск номера по номеру комнаты
    public Room getRoomByNumber(int number) {
        for (Room room : rooms) {
            if (room.getNumber() == number) return room;
        }
        return null;
    }

    // геттеры
    public List<Room> getRooms() {
        return rooms;
    }

    public List<Service> getServices() {
        return services;
    }
}
