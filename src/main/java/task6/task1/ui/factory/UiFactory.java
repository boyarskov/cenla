package task6.task1.ui.factory;

import task6.task1.ui.menu.Menu;
import java.util.Map;

public interface UiFactory {
    // Возвращает все меню, связанные между собой
    Map<task6.task1.ui.menu.MenuId, Menu> createMenus();
}