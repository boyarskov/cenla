package task8.task1.model;

import task8.task1.model.enums.RoomStatus;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Room implements Serializable {
    private int number;
    private double price;
    private int capacity;
    private RoomStatus status;
    private Guest guest;
    private static int nextId = 1;
    private int id;
    private List<Booking> bookingHistory;
    private int maxHistoryEntries = 3;

    public Room(int number, double price, int capacity) {
        this.id = nextId++;
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.status = RoomStatus.FREE;
        this.bookingHistory = new ArrayList<>();
    }

    public void setMaxHistoryEntries(int maxHistoryEntries) {
        this.maxHistoryEntries = maxHistoryEntries;
    }

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
        if (bookingHistory.size() > maxHistoryEntries) {
            bookingHistory.remove(0);
        }
    }

    public List<Booking> getBookingHistory() {
        return bookingHistory;
    }

    @Override
    public String toString() {
        return "Номер " + number + " | вместимость: " + capacity + " чел | " +
                price + " руб | статус: " + status;
    }
}
