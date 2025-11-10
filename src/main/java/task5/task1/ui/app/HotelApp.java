package task5.task1.ui.app;

import task5.task1.manager.HotelManager;
import task5.task1.model.*;
import task5.task1.ui.MenuController;
import task5.task1.ui.factory.ConsoleUiFactory;
import task5.task1.ui.factory.UiFactory;

import java.util.Scanner;

public class HotelApp {
    public static void main(String[] args) {
        // Модель и сервис
        Hotel hotel = new Hotel();
        // Пример наполнения (можно перенести в демо-дату)
        hotel.addRoom(new Room(101, 100.0, 1));
        hotel.addRoom(new Room(102, 150.0, 2));
        hotel.addRoom(new Room(201, 200.0, 3));
        hotel.addService(new Service("Breakfast", 10));
        hotel.addService(new Service("Parking", 5));

        HotelManager manager = new HotelManager(hotel);

        // UI
        Scanner in = new Scanner(System.in);
        UiFactory factory = new ConsoleUiFactory(manager, in);
        factory.createMenus(); // инициализирует меню и установит корневое меню в Navigator

        new MenuController().run(in);
    }
}