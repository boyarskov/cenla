package task6.task1.ui.factory;

import task6.task1.manager.HotelManager;
import task6.task1.manager.HotelException;
import task6.task1.model.*;
import task6.task1.model.enums.*;
import task6.task1.ui.Navigator;
import task6.task1.ui.menu.*;
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

        createRootMenu(root, rooms, guests, services, reports);
        createRoomsMenu(rooms, root);
        createGuestsMenu(guests, root);
        createServicesMenu(services, root);
        createReportsMenu(reports, root);

        map.put(MenuId.ROOT, root);
        map.put(MenuId.ROOMS, rooms);
        map.put(MenuId.GUESTS, guests);
        map.put(MenuId.SERVICES, services);
        map.put(MenuId.REPORTS, reports);
        map.put(MenuId.EXIT, exit);

        nav.setCurrentMenu(root);
        return map;
    }

    private void createRootMenu(Menu root, Menu rooms, Menu guests, Menu services, Menu reports) {
        root.addItem(new MenuItem("Номера", null, rooms))
                .addItem(new MenuItem("Гости", null, guests))
                .addItem(new MenuItem("Услуги", null, services))
                .addItem(new MenuItem("Отчёты", null, reports))
                .addItem(new MenuItem("Выход", () -> System.exit(0), null));
    }

    private void createRoomsMenu(Menu rooms, Menu root) {
        rooms.addItem(new MenuItem("Показать все номера (по цене)",
                () -> manager.showAllRooms(SortType.BY_PRICE), null));

        rooms.addItem(new MenuItem("Показать свободные номера (по вместимости)",
                () -> manager.showFreeRooms(SortType.BY_CAPACITY), null));

        rooms.addItem(new MenuItem("Заселить гостя",
                () -> runSafe(() -> {
                    String name = ask("Имя гостя: ");
                    int number = askInt("Номер комнаты: ");
                    manager.checkIn(new Guest(name), number);
                }), null));


        rooms.addItem(new MenuItem("Выселить гостя",
                () -> runSafe(() -> {
                    int number = askInt("Номер комнаты: ");
                    manager.checkOut(number);
                }), null));

        rooms.addItem(new MenuItem("Изменить статус комнаты",
                () -> runSafe(() -> {
                    int number = askInt("Номер комнаты: ");
                    RoomStatus status = askEnum(RoomStatus.class, "Статус (FREE/OCCUPIED/REPAIR/SERVICE): ");
                    manager.changeRoomStatus(number, status);
                }), null));

        rooms.addItem(new MenuItem("Изменить цену комнаты",
                () -> runSafe(() -> {
                    int number = askInt("Номер комнаты: ");
                    double price = askDouble("Новая цена: ");
                    manager.changeRoomPrice(number, price);
                }), null));

        rooms.addItem(new MenuItem("Детали комнаты",
                () -> runSafe(() -> {
                    int number = askInt("Номер комнаты: ");
                    manager.showRoomDetails(number);
                }), null));

        rooms.addItem(new MenuItem("Экспорт номеров в CSV", () -> {
            System.out.print("Введите имя файла для экспорта номеров: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.exportRooms(fileName);
        }, null));

        rooms.addItem(new MenuItem("Импорт номеров из CSV", () -> {
            System.out.print("Введите имя файла для импорта номеров: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.importRooms(fileName);
            manager.rebuildRelations();
        }, null));

        rooms.addItem(new MenuItem("Назад", null, root));
    }

    private void createGuestsMenu(Menu guests, Menu root) {
        guests.addItem(new MenuItem("Список гостей (по имени)",
                () -> manager.showGuests(SortType.BY_NAME), null));

        guests.addItem(new MenuItem("Список гостей (по дате)",
                () -> manager.showGuests(SortType.BY_DATE), null));

        guests.addItem(new MenuItem("Добавить услугу гостю",
                () -> {
                    int number = askInt("Номер комнаты (где живёт гость): ");
                    String serviceName = ask("Название услуги: ");
                    double price = askDouble("Цена: ");

                    Room room = manager.getHotel().getRoomByNumber(number);
                    if (room == null || room.getGuest() == null) {
                        System.out.println("Комната не найдена или свободна.");
                        return;
                    }

                    room.getGuest().addService(new Service(serviceName, price));
                }, null));

        guests.addItem(new MenuItem("Экспорт гостей в CSV", () -> {
            System.out.print("Введите имя файла для экспорта гостей: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.exportGuests(fileName);
        }, null));

        guests.addItem(new MenuItem("Импорт гостей из CSV", () -> {
            System.out.print("Введите имя файла для импорта гостей: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.importGuests(fileName);
            manager.rebuildRelations();
        }, null));

        guests.addItem(new MenuItem("Назад", null, root));
    }

    private void createServicesMenu(Menu services, Menu root) {
        services.addItem(new MenuItem("Изменить цену услуги по имени",
                () -> runSafe(() -> {
                    String name = ask("Название услуги: ");
                    double price = askDouble("Новая цена: ");
                    manager.changeServicePrice(name, price);
                }), null));

        services.addItem(new MenuItem("Экспорт услуг в CSV", () -> {
            System.out.print("Введите имя файла для экспорта услуг: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.exportServices(fileName);
        }, null));

        services.addItem(new MenuItem("Импорт услуг из CSV", () -> {
            System.out.print("Введите имя файла для импорта услуг: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.importServices(fileName);
        }, null));

        services.addItem(new MenuItem("Назад", null, root));
    }

    private void createReportsMenu(Menu reports, Menu root) {
        reports.addItem(new MenuItem("Всего свободных номеров",
                () -> System.out.println("Free: " + manager.getTotalFreeRooms()), null));

        reports.addItem(new MenuItem("Всего занятых номеров",
                () -> System.out.println("Occupied: " + manager.getTotalGuests()), null));

        reports.addItem(new MenuItem("Экспорт бронирований в CSV", () -> {
            System.out.print("Введите имя файла для экспорта бронирований: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.exportBookings(fileName);
        }, null));

        reports.addItem(new MenuItem("Импорт бронирований из CSV", () -> {
            System.out.print("Введите имя файла для импорта бронирований: ");
            String fileName = in.nextLine().trim();
            if (fileName.isEmpty()) {
                System.out.println("Имя файла не должно быть пустым.");
                return;
            }
            manager.importBookings(fileName);
            manager.rebuildRelations();
        }, null));

        reports.addItem(new MenuItem("Назад", null, root));
    }

    private void runSafe(Runnable action) {
        try {
            action.run();
        } catch (HotelException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Внутренняя ошибка приложения: " + e.getMessage());
        }
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