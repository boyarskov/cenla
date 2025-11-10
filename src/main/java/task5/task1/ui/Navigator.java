package task5.task1.ui;

import task5.task1.ui.menu.Menu;

public class Navigator {
    private static final Navigator INSTANCE = new Navigator();
    private Menu currentMenu;

    private Navigator() {}

    public static Navigator getInstance() { return INSTANCE; }

    public Menu getCurrentMenu() { return currentMenu; }
    public void setCurrentMenu(Menu menu) { this.currentMenu = menu; }

    public void printMenu() {
        System.out.println("\n== " + currentMenu.getName() + " ==");
        int i = 1;
        for (var item : currentMenu.getItems()) {
            System.out.println(i++ + ") " + item.getTitle());
        }
        System.out.print("Выбор: ");
    }

    public void navigate(int index) {
        if (index < 1 || index > currentMenu.getItems().size()) {
            System.out.println("Некорректный пункт меню.");
            return;
        }
        var item = currentMenu.getItems().get(index - 1);
        item.doAction();
        if (item.getNextMenu() != null) {
            setCurrentMenu(item.getNextMenu());
        }
    }
}