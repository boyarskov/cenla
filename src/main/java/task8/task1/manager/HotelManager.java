package task8.task1.manager;

import task8.task1.model.*;
import task8.task1.model.*;
import task8.task1.model.enums.RoomStatus;
import task8.task1.model.enums.SortType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HotelManager {
    private Hotel hotel;
    private Config config;
    private List<Guest> standaloneGuests = new ArrayList<>();

    public HotelManager(Hotel hotel, Config config) {
        this.hotel = hotel;
        this.config = config;
    }

    public void checkIn(Guest guest, int roomNumber) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new HotelException("Номер " + roomNumber + " не найден.");
        }
        if (room.getStatus() != RoomStatus.FREE) {
            throw new HotelException("Номер " + roomNumber + " занят или недоступен.");
        }

        room.setGuest(guest);
        room.setStatus(RoomStatus.OCCUPIED);

        Booking booking = new Booking(guest, LocalDate.now(), null);
        room.addBooking(booking);

        System.out.println("Гость " + guest.getName() + " заселен в номер " + roomNumber);
    }

    public void checkOut(int roomNumber) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new HotelException("Номер " + roomNumber + " не найден.");
        }
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new HotelException("Нельзя выселить: номер " + roomNumber + " свободен или недоступен.");
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
        if (!config.isAllowStatusChange()) {
            System.out.println("Изменение статуса номера запрещено настройками (allowStatusChange=false).");
            return;
        }
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new HotelException("Номер " + roomNumber + " не найден.");
        }
        room.setStatus(newStatus);
        System.out.println("Статус номера " + roomNumber + " изменен на " + newStatus);
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        Room room = hotel.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new HotelException("Номер " + roomNumber + " не найден.");
        }
        room.setPrice(newPrice);
        System.out.println("Цена номера " + roomNumber + " изменена на " + newPrice + " руб.");
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        for (Service s : hotel.getServices()) {
            if (s.getName().equalsIgnoreCase(serviceName)) {
                s.setPrice(newPrice);
                System.out.println("Цена услуги '" + s.getName() + "' изменена на " + newPrice + " руб.");
                return;
            }
        }
        throw new HotelException("Услуга '" + serviceName + "' не найдена.");
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

    // Собираем всех гостей: из комнат, из истории бронирований и из standaloneGuests
    private List<Guest> collectAllGuests() {
        List<Guest> result = new ArrayList<>();

        // Гости, импортированные отдельно
        for (Guest g : standaloneGuests) {
            if (!containsGuestWithId(result, g.getId())) {
                result.add(g);
            }
        }

        // Гости из текущих заселений и историй бронирований
        for (Room room : hotel.getRooms()) {
            Guest current = room.getGuest();
            if (current != null && !containsGuestWithId(result, current.getId())) {
                result.add(current);
            }

            for (Booking b : room.getBookingHistory()) {
                Guest bg = b.getGuest();
                if (bg != null && !containsGuestWithId(result, bg.getId())) {
                    result.add(bg);
                }
            }
        }

        return result;
    }

    private boolean containsGuestWithId(List<Guest> guests, int id) {
        for (Guest g : guests) {
            if (g.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private Guest findGuestById(int id) {
        // Сначала ищем среди standalone-гостей
        for (Guest g : standaloneGuests) {
            if (g.getId() == id) {
                return g;
            }
        }

        // Потом ищем среди гостей в комнатах и бронированиях
        for (Room room : hotel.getRooms()) {
            Guest current = room.getGuest();
            if (current != null && current.getId() == id) {
                return current;
            }
            for (Booking b : room.getBookingHistory()) {
                Guest bg = b.getGuest();
                if (bg != null && bg.getId() == id) {
                    return bg;
                }
            }
        }

        return null;
    }

    private Room findRoomById(int id) {
        for (Room room : hotel.getRooms()) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    private Service findServiceById(int id) {
        for (Service service : hotel.getServices()) {
            if (service.getId() == id) {
                return service;
            }
        }
        return null;
    }

    private Booking findBookingById(int id) {
        for (Room room : hotel.getRooms()) {
            for (Booking b : room.getBookingHistory()) {
                if (b.getId() == id) {
                    return b;
                }
            }
        }
        return null;
    }

    private void removeBookingFromRooms(Booking booking) {
        for (Room room : hotel.getRooms()) {
            room.getBookingHistory().remove(booking);
        }
    }

    public void exportGuests(String fileName) {
        List<Guest> guests = collectAllGuests();

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("id;name");
            for (Guest guest : guests) {
                writer.println(guest.getId() + ";" + guest.getName());
            }
            System.out.println("Экспорт гостей завершен. Файл: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при экспорте гостей: " + e.getMessage());
        }
    }

    public void exportServices(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("id;name;price");
            for (Service service : hotel.getServices()) {
                writer.println(service.getId() + ";" + service.getName() + ";" + service.getPrice());
            }
            System.out.println("Экспорт услуг завершен. Файл: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при экспорте услуг: " + e.getMessage());
        }
    }

    public void exportRooms(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("id;number;capacity;price;status;currentGuestId");
            for (Room room : hotel.getRooms()) {
                String guestIdStr = "";
                Guest guest = room.getGuest();
                if (guest != null) {
                    guestIdStr = String.valueOf(guest.getId());
                }

                writer.println(
                        room.getId() + ";" +
                                room.getNumber() + ";" +
                                room.getCapacity() + ";" +
                                room.getPrice() + ";" +
                                room.getStatus().name() + ";" +
                                guestIdStr
                );
            }
            System.out.println("Экспорт комнат завершен. Файл: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при экспорте комнат: " + e.getMessage());
        }
    }

    public void exportBookings(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("id;roomId;guestId;checkIn;checkOut");

            for (Room room : hotel.getRooms()) {
                int roomId = room.getId();
                for (Booking booking : room.getBookingHistory()) {
                    Guest guest = booking.getGuest();
                    String guestIdStr = guest != null ? String.valueOf(guest.getId()) : "";
                    String checkOutStr = booking.getCheckOutDate() != null
                            ? booking.getCheckOutDate().toString()
                            : "";

                    writer.println(
                            booking.getId() + ";" +
                                    roomId + ";" +
                                    guestIdStr + ";" +
                                    booking.getCheckInDate().toString() + ";" +
                                    checkOutStr
                    );
                }
            }

            System.out.println("Экспорт бронирований завершен. Файл: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при экспорте бронирований: " + e.getMessage());
        }
    }

    public void importGuests(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // пропускаем заголовок
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length < 2) {
                    System.out.println("Неверный формат строки (гость): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();

                    Guest guest = findGuestById(id);
                    if (guest != null) {
                        // обновляем имя существующего гостя
                        guest.setName(name);
                    } else {
                        // создаём нового гостя и кладём в standalone-список
                        guest = new Guest(name);
                        guest.setId(id);
                        standaloneGuests.add(guest);
                    }

                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (Exception ex) {
                    System.out.println("Не удалось разобрать строку (гость): " + line);
                }
            }

            System.out.println("Импорт гостей завершен из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при импорте гостей: " + e.getMessage());
        }
    }

    public void importServices(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // заголовок
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length < 3) {
                    System.out.println("Неверный формат строки (услуга): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());

                    Service service = findServiceById(id);
                    if (service != null) {
                        // обновляем существующую услугу
                        service.setPrice(price);
                    } else {
                        // добавляем новую услугу
                        Service newService = new Service(name, price);
                        newService.setId(id);
                        hotel.getServices().add(newService);
                    }

                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (Exception ex) {
                    System.out.println("Не удалось разобрать строку (услуга): " + line);
                }
            }

            System.out.println("Импорт услуг завершен из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при импорте услуг: " + e.getMessage());
        }
    }

    public void importRooms(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // заголовок
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length < 5) {
                    System.out.println("Неверный формат строки (комната): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int number = Integer.parseInt(parts[1].trim());
                    int capacity = Integer.parseInt(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());
                    RoomStatus status = RoomStatus.valueOf(parts[4].trim());

                    Guest guest = null;
                    if (parts.length >= 6 && !parts[5].trim().isEmpty()) {
                        int guestId = Integer.parseInt(parts[5].trim());
                        guest = findGuestById(guestId);
                    }

                    Room room = findRoomById(id);
                    if (room != null) {
                        // обновляем существующую комнату
                        room.setPrice(price);
                        room.setStatus(status);
                        room.setGuest(guest);
                    } else {
                        // создаём новую комнату
                        Room newRoom = new Room(number, price, capacity);
                        newRoom.setId(id);
                        newRoom.setStatus(status);
                        newRoom.setGuest(guest);
                        hotel.addRoom(newRoom);
                    }

                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (Exception ex) {
                    System.out.println("Не удалось разобрать строку (комната): " + line);
                }
            }

            System.out.println("Импорт комнат завершен из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при импорте комнат: " + e.getMessage());
        }
    }

    public void importBookings(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length < 4) {
                    System.out.println("Неверный формат строки (бронь): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int roomId = Integer.parseInt(parts[1].trim());
                    String guestIdStr = parts[2].trim();
                    int guestId = guestIdStr.isEmpty() ? -1 : Integer.parseInt(guestIdStr);
                    LocalDate checkIn = LocalDate.parse(parts[3].trim());
                    LocalDate checkOut = null;
                    if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                        checkOut = LocalDate.parse(parts[4].trim());
                    }

                    Room room = findRoomById(roomId);
                    if (room == null) {
                        System.out.println("Не найдена комната с id " + roomId + " для бронирования: " + line);
                        continue;
                    }

                    Guest guest = null;
                    if (guestId != -1) {
                        guest = findGuestById(guestId);
                        if (guest == null) {
                            guest = new Guest("Guest#" + guestId);
                            guest.setId(guestId);
                            standaloneGuests.add(guest);
                        }
                    }

                    Booking existing = findBookingById(id);
                    if (existing != null) {
                        removeBookingFromRooms(existing);
                    }

                    Booking newBooking = new Booking(guest, checkIn, checkOut);
                    newBooking.setId(id);
                    room.getBookingHistory().add(newBooking);

                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (Exception ex) {
                    System.out.println("Не удалось разобрать строку (бронь): " + line);
                }
            }

            System.out.println("Импорт бронирований завершен из файла: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при импорте бронирований: " + e.getMessage());
        }
    }

    public void rebuildRelations() {
        for (Room room : hotel.getRooms()) {
            List<Booking> history = room.getBookingHistory();

            if (history == null || history.isEmpty()) {
                room.setGuest(null);
                if (room.getStatus() == RoomStatus.OCCUPIED) {
                    room.setStatus(RoomStatus.FREE);
                }
                continue;
            }

            Booking last = history.get(history.size() - 1);

            if (last.getCheckOutDate() == null || last.getCheckOutDate().isAfter(LocalDate.now())) {
                room.setGuest(last.getGuest());
                room.setStatus(RoomStatus.OCCUPIED);
            } else {
                room.setGuest(null);
                if (room.getStatus() == RoomStatus.OCCUPIED) {
                    room.setStatus(RoomStatus.FREE);
                }
            }
        }
    }
}