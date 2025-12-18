package task8.task1.ui.app;

import task8.task1.manager.HotelManager;
import task8.task1.manager.Config;
import task8.task1.model.Hotel;
import task8.task1.model.Room;
import task8.task1.model.Service;
import task8.task1.ui.MenuController;
import task8.task1.ui.Navigator;
import task8.task1.ui.factory.ConsoleUiFactory;
import task8.task1.ui.factory.UiFactory;
import task8.task1.framework.di.SimpleInjector;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class HotelApp {
    public static void main(String[] args) {
        Config config = new Config("config.properties");

        Hotel hotel = loadHotel();
        if (hotel == null) {
            hotel = new Hotel();

            Room room101 = new Room(101, 100.0, 1);
            room101.setMaxHistoryEntries(config.getMaxGuestHistoryEntries());
            hotel.addRoom(room101);

            Room room102 = new Room(102, 150.0, 2);
            room102.setMaxHistoryEntries(config.getMaxGuestHistoryEntries());
            hotel.addRoom(room102);

            Room room201 = new Room(201, 200.0, 3);
            room201.setMaxHistoryEntries(config.getMaxGuestHistoryEntries());
            hotel.addRoom(room201);

            hotel.addService(new Service("Breakfast", 10));
            hotel.addService(new Service("Parking", 5));
        } else {
            // Обновляем лимит истории для уже существующих комнат из конфига
            for (Room room : hotel.getRooms()) {
                room.setMaxHistoryEntries(config.getMaxGuestHistoryEntries());
            }
        }

        final Hotel finalHotel = hotel;

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("Сохраняем состояние отеля перед выходом.");
                    saveHotel(finalHotel);
                })
        );

        HotelManager manager = new HotelManager(finalHotel, config);

        Scanner in = new Scanner(System.in);

        UiFactory factory = new ConsoleUiFactory();
        MenuController controller = new MenuController();
        Navigator navigator = Navigator.getInstance();

        Map<Class<?>, Object> instances = new HashMap<>();
        instances.put(HotelManager.class, manager);
        instances.put(Scanner.class, in);
        instances.put(Navigator.class, navigator);

        SimpleInjector injector = new SimpleInjector();
        injector.injectDependencies(factory, instances);
        injector.injectDependencies(controller, instances);

        factory.createMenus();
        controller.run(in);
        saveHotel(finalHotel);
    }

    private static Hotel loadHotel() {
        try (FileInputStream fis = new FileInputStream("hotel_state.bin");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            System.out.println("Состояние отеля загружено из hotel_state.bin");
            return (Hotel) obj;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Сохранённый файл hotel_state.bin не найден или не читается.");
            return null;
        }
    }

    private static void saveHotel(Hotel hotel) {
        if (hotel == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream("hotel_state.bin");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(hotel);
            System.out.println("Состояние отеля сохранено в файл hotel_state.bin");

        } catch (IOException e) {
            System.out.println("Не удалось сохранить состояние отеля: " + e.getMessage());
        }
    }
}