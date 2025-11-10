package task5.task1.ui;

import task5.task1.ui.menu.Menu;
import java.util.Scanner;

public class MenuController {
    private final Navigator navigator;

    public MenuController() {
        this.navigator = Navigator.getInstance();
    }

    public void run(Scanner in) {
        while (true) {
            navigator.printMenu();
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                int index = Integer.parseInt(line);
                navigator.navigate(index);
            } catch (NumberFormatException e) {
                System.out.println("Введите номер пункта меню.");
            }
        }
    }
}