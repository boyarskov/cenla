package task6.task1.model;

import task6.task1.model.enums.RoomStatus;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int number;          // номер комнаты
    private double price;        // цена за ночь
    private int capacity;        // вместимость (сколько человек)
    private RoomStatus status;   // текущий статус
    private Guest guest;         // текущий постоялец (если есть)
    private static int nextId = 1;
    private int id;

    private List<Booking> bookingHistory;

    public Room(int number, double price, int capacity) {
        this.id = nextId++;
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.status = RoomStatus.FREE; // по умолчанию свободен
        this.bookingHistory = new ArrayList<>(); // инициализация списка историй
    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
        if (bookingHistory.size() > 3) {
            bookingHistory.remove(0); // храним только 3 последних
        }
    }

    // Получить последние брони
    public List<Booking> getBookingHistory() {
        return bookingHistory;
    }

    @Override
    public String toString() {
        return "Номер " + number + " | вместимость: " + capacity + " чел | " +
                price + " руб | статус: " + status;
    }
}
