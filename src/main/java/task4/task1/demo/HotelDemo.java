package task4.task1.demo;

import task4.task1.manager.HotelManager;
import task4.task1.model.*;
import task4.task1.model.enums.RoomStatus;
import task4.task1.model.enums.SortType;

public class HotelDemo {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        HotelManager manager = new HotelManager(hotel);

        hotel.addRoom(new Room(101, 2500, 2));
        hotel.addRoom(new Room(102, 3500, 3));
        hotel.addRoom(new Room(103, 2000, 1));
        hotel.addRoom(new Room(104, 4000, 4));

        hotel.addService(new Service("Завтрак", 500));
        hotel.addService(new Service("Уборка", 300));
        hotel.addService(new Service("Массаж", 1500));

        Guest guest1 = new Guest("Иван Иванов");
        Guest guest2 = new Guest("Мария Петрова");
        Guest guest3 = new Guest("Алексей Сидоров");

        manager.checkIn(guest1, 101);
        manager.checkIn(guest2, 102);

        manager.changeRoomStatus(103, RoomStatus.REPAIR);
        manager.changeRoomPrice(104, 4200);
        manager.changeServicePrice("Массаж", 1800);

        guest1.addService(new Service("Завтрак", 600));
        guest1.addService(new Service("Массаж", 1800));
        guest2.addService(new Service("Уборка", 300));

        System.out.println("\nСписок всех номеров (по цене)");
        manager.showAllRooms(SortType.BY_PRICE);

        System.out.println("\nСвободные номера (по вместимости)");
        manager.showFreeRooms(SortType.BY_CAPACITY);

        System.out.println("\nСписок постояльцев (по алфавиту)");
        manager.showGuests(SortType.BY_NAME);

        System.out.println("\nПодсчёты");
        manager.getTotalFreeRooms();
        manager.getTotalGuests();

        manager.checkOut(101);
        manager.checkOut(102);

        System.out.println("\nИстория проживания по номеру 101");
        manager.showRoomDetails(101);

        manager.checkIn(guest3, 101);
        manager.checkOut(101);

        manager.checkIn(new Guest("Наталья Романова"), 101);
        manager.checkOut(101);

        manager.checkIn(new Guest("Павел Кузнецов"), 101);
        manager.checkOut(101);

        System.out.println("\nПоследние 3 постояльца номера 101");
        manager.showRoomDetails(101);
    }
}
