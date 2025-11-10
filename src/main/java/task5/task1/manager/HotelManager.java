package task5.task1.manager;

import task5.task1.model.*;
import task5.task1.model.enums.RoomStatus;
import task5.task1.model.enums.SortType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HotelManager {
    private Hotel hotel;

    public HotelManager(Hotel hotel) {
        this.hotel = hotel;
    }

    public void checkIn(Guest guest, int roomNumber) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Ошибка: номер " + roomNumber + " не найден.");
            return;
        }
        if (room.getStatus() != RoomStatus.FREE) {
            System.out.println("Ошибка: номер " + roomNumber + " занят или недоступен.");
            return;
        }

        room.setGuest(guest);
        room.setStatus(RoomStatus.OCCUPIED);

        Booking booking = new Booking(guest, LocalDate.now(), null);
        room.addBooking(booking);

        System.out.println("Гость " + guest.getName() + " заселен в номер " + roomNumber);
    }

    public void checkOut(int roomNumber) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null || room.getStatus() != RoomStatus.OCCUPIED) {
            System.out.println("Ошибка: нельзя выселить, номер " + roomNumber + " свободен или не существует.");
            return;
        }

        List<Booking> history = room.getBookingHistory();
        if (!history.isEmpty()) {
            Booking last = history.get(history.size() - 1);
            history.set(history.size() - 1,
                    new Booking(last.getGuest(), last.getCheckInDate(), LocalDate.now()));
        }

        System.out.println("Гость " + room.getGuest().getName() + " выселен из номера " + roomNumber);
        room.setGuest(null);
        room.setStatus(RoomStatus.FREE);
    }

    public void changeRoomStatus(int roomNumber, RoomStatus newStatus) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Ошибка: номер " + roomNumber + " не найден.");
            return;
        }
        room.setStatus(newStatus);
        System.out.println("Статус номера " + roomNumber + " изменен на " + newStatus);
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room != null) {
            room.setPrice(newPrice);
            System.out.println("Цена номера " + roomNumber + " изменена на " + newPrice + " руб.");
        }
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        for (Service s : hotel.getServices()) {
            if (s.getName().equalsIgnoreCase(serviceName)) {
                s.setPrice(newPrice);
                System.out.println("Цена услуги '" + s.getName() + "' изменена на " + newPrice + " руб.");
                return;
            }
        }
        System.out.println("Услуга '" + serviceName + "' не найдена.");
    }

    public void showAllRooms(SortType sortType) {
        List<Room> sorted = sortRooms(hotel.getRooms(), sortType);
        System.out.println("Список всех номеров:");
        sorted.forEach(System.out::println);
    }

    public void showFreeRooms(SortType sortType) {
        List<Room> free = hotel.getRooms().stream()
                .filter(r -> r.getStatus() == RoomStatus.FREE)
                .collect(Collectors.toList());
        List<Room> sorted = sortRooms(free, sortType);

        System.out.println("Свободные номера:");
        sorted.forEach(System.out::println);
    }

    private List<Room> sortRooms(List<Room> rooms, SortType sortType) {
        switch (sortType) {
            case BY_PRICE:
                return rooms.stream()
                        .sorted(Comparator.comparing(Room::getPrice))
                        .collect(Collectors.toList());
            case BY_CAPACITY:
                return rooms.stream()
                        .sorted(Comparator.comparing(Room::getCapacity))
                        .collect(Collectors.toList());
            default:
                return rooms;
        }
    }

    public void showGuests(SortType sortType) {
        List<Room> occupied = hotel.getRooms().stream()
                .filter(r -> r.getStatus() == RoomStatus.OCCUPIED)
                .collect(Collectors.toList());

        switch (sortType) {
            case BY_NAME:
                occupied.sort(Comparator.comparing(r -> r.getGuest().getName()));
                break;
            case BY_DATE:
                occupied.sort(Comparator.comparing(r -> {
                    List<Booking> h = r.getBookingHistory();
                    return h.isEmpty() ? LocalDate.MIN : h.get(h.size() - 1).getCheckOutDate();
                }));
                break;
        }

        System.out.println("Список постояльцев:");
        for (Room r : occupied) {
            System.out.println(r.getGuest().getName() + " — номер " + r.getNumber());
        }
    }

    public int getTotalFreeRooms() {
        long count = hotel.getRooms().stream()
                .filter(r -> r.getStatus() == RoomStatus.FREE)
                .count();
        System.out.println("Всего свободных номеров: " + count);

        return (int) count;
    }

    public int getTotalGuests() {
        long count = hotel.getRooms().stream()
                .filter(r -> r.getStatus() == RoomStatus.OCCUPIED)
                .count();
        System.out.println("Общее число постояльцев: " + count);

        return (int) count;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void showRoomDetails(int roomNumber) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Номер " + roomNumber + " не найден.");
            return;
        }

        System.out.println("Детали номера:");
        System.out.println(room);
        if (room.getGuest() != null) {
            System.out.println("Текущий гость: " + room.getGuest().getName());
        }
        System.out.println("История проживания:");
        for (Booking b : room.getBookingHistory()) {
            System.out.println("  - " + b);
        }
    }
}