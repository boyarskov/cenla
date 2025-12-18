package task8.task1.ui.factory;

import task8.task1.ui.menu.Menu;
import task8.task1.ui.menu.MenuId;

import java.util.Map;

public interface UiFactory {
    // Возвращает все меню, связанные между собой
    Map<MenuId, Menu> createMenus();
}