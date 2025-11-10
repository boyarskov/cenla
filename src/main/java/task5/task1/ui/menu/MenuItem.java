package task5.task1.ui.menu;

import task5.task1.ui.IAction;

public class MenuItem {
    private final String title;
    private final IAction action;
    private final Menu nextMenu;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public String getTitle() { return title; }
    public Menu getNextMenu() { return nextMenu; }

    public void doAction() {
        if (action != null) action.execute();
    }
}