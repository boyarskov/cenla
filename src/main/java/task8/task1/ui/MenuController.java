package task8.task1.ui;

import java.util.Scanner;
import task8.task1.annotations.di.Inject;
import task8.task1.ui.Navigator;

public class MenuController {

    @Inject
    private Navigator navigator;

    public MenuController() {
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