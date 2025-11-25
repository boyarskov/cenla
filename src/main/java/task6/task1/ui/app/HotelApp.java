package task6.task1.ui.app;

import task6.task1.manager.HotelManager;
import task6.task1.manager.Config;
import task6.task1.model.*;
import task6.task1.ui.MenuController;
import task6.task1.ui.factory.ConsoleUiFactory;
import task6.task1.ui.factory.UiFactory;

import java.util.Scanner;

public class HotelApp {
    public static void main(String[] args) {
        Config config = new Config("config.properties");
        Hotel hotel = new Hotel();

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

        HotelManager manager = new HotelManager(hotel, config);

        Scanner in = new Scanner(System.in);
        UiFactory factory = new ConsoleUiFactory(manager, in);
        factory.createMenus();

        new MenuController().run(in);
    }
}