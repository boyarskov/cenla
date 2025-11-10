package task5.task1.ui.factory;


import task5.task1.manager.HotelManager;
import task5.task1.model.*;
import task5.task1.model.enums.*;
import task5.task1.ui.Navigator;
import task5.task1.ui.menu.*;

import java.util.*;

public class ConsoleUiFactory implements UiFactory {

    private final HotelManager manager;
    private final Scanner in;
    private final Navigator nav = Navigator.getInstance();

    public ConsoleUiFactory(HotelManager manager, Scanner in) {
        this.manager = manager;
        this.in = in;
    }

    @Override
    public Map<MenuId, Menu> createMenus() {
        Map<MenuId, Menu> map = new EnumMap<>(MenuId.class);

        Menu root = new Menu("Главное меню");
        Menu rooms = new Menu("Номера");
        Menu guests = new Menu("Гости");
        Menu services = new Menu("Услуги");
        Menu reports = new Menu("Отчёты");
        Menu exit = new Menu("Выход");

        // ROOT
        root.addItem(new MenuItem("Номера", null, rooms))
                .addItem(new MenuItem("Гости", null, guests))
                .addItem(new MenuItem("Услуги", null, services))
                .addItem(new MenuItem("Отчёты", null, reports))
                .addItem(new MenuItem("Выход", () -> System.exit(0), null));

        // ROOMS
        rooms.addItem(new MenuItem("Показать все номера (по цене)",
                () -> manager.showAllRooms(SortType.BY_PRICE), null));
        rooms.addItem(new MenuItem("Показать свободные номера (по вместимости)",
                () -> manager.showFreeRooms(SortType.BY_CAPACITY), null));
        rooms.addItem(new MenuItem("Заселить гостя",
                () -> {
                    String name = ask("Имя гостя: ");
                    int number = askInt("Номер комнаты: ");
                    manager.checkIn(new Guest(name), number);
                }, null));
        rooms.addItem(new MenuItem("Выселить гостя",
                () -> {
                    int number = askInt("Номер комнаты: ");
                    manager.checkOut(number);
                }, null));
        rooms.addItem(new MenuItem("Изменить статус комнаты",
                () -> {
                    int number = askInt("Номер комнаты: ");
                    RoomStatus status = askEnum(RoomStatus.class, "Статус (FREE/OCCUPIED/REPAIR/SERVICE): ");
                    manager.changeRoomStatus(number, status);
                }, null));
        rooms.addItem(new MenuItem("Изменить цену комнаты",
                () -> {
                    int number = askInt("Номер комнаты: ");
                    double price = askDouble("Новая цена: ");
                    manager.changeRoomPrice(number, price);
                }, null));
        rooms.addItem(new MenuItem("Детали комнаты",
                () -> {
                    int number = askInt("Номер комнаты: ");
                    manager.showRoomDetails(number);
                }, null));
        rooms.addItem(new MenuItem("Назад", null, root));

        // GUESTS
        guests.addItem(new MenuItem("Список гостей (по имени)",
                () -> manager.showGuests(SortType.BY_NAME), null));
        guests.addItem(new MenuItem("Список гостей (по дате)",
                () -> manager.showGuests(SortType.BY_DATE), null));
        guests.addItem(new MenuItem("Добавить услугу гостю",
                () -> {
                    int number = askInt("Номер комнаты (где живёт гость): ");
                    String serviceName = ask("Название услуги: ");
                    double price = askDouble("Цена: ");
                    // Находим текущего гостя комнаты
                    Room room = manager.getHotel().getRoomByNumber(number);
                    if (room == null || room.getGuest() == null) {
                        System.out.println("Комната не найдена или свободна.");
                        return;
                    }
                    room.getGuest().addService(new Service(serviceName, price));
                }, null));
        guests.addItem(new MenuItem("Назад", null, root));

        // SERVICES
        services.addItem(new MenuItem("Изменить цену услуги по имени",
                () -> {
                    String name = ask("Название услуги: ");
                    double price = askDouble("Новая цена: ");
                    manager.changeServicePrice(name, price);
                }, null));
        services.addItem(new MenuItem("Назад", null, root));

        // REPORTS
        reports.addItem(new MenuItem("Всего свободных номеров",
                () -> System.out.println("Free: " + manager.getTotalFreeRooms()), null));
        reports.addItem(new MenuItem("Всего занятых номеров",
                () -> System.out.println("Occupied: " + manager.getTotalGuests()), null));
        reports.addItem(new MenuItem("Назад", null, root));

        map.put(MenuId.ROOT, root);
        map.put(MenuId.ROOMS, rooms);
        map.put(MenuId.GUESTS, guests);
        map.put(MenuId.SERVICES, services);
        map.put(MenuId.REPORTS, reports);
        map.put(MenuId.EXIT, exit);

        nav.setCurrentMenu(root);
        return map;
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private int askInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private double askDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число (double).");
            }
        }
    }

    private <E extends Enum<E>> E askEnum(Class<E> type, String prompt) {
        System.out.print(prompt);
        String val = in.nextLine().trim().toUpperCase();
        try {
            return Enum.valueOf(type, val);
        } catch (IllegalArgumentException e) {
            System.out.println("Недопустимое значение. Повторите.");
            return askEnum(type, prompt);
        }
    }
}