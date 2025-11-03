package task4.task1.model;

import java.time.LocalDate;

public class Booking {
    private Guest guest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Booking(Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
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
