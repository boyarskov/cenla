package task6.task1.model;

import java.time.LocalDate;
import java.io.Serializable;

public class Booking implements Serializable {
    private Guest guest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private static int nextId = 1;
    private int id;

    public Booking (Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        this.id = nextId++;
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return guest.getName() + " [" + checkInDate + " - " + checkOutDate + "]";
    }
}
