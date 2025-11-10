package task5.task1.ui.factory;

import task5.task1.ui.menu.Menu;
import java.util.Map;

public interface UiFactory {
    // Возвращает все меню, связанные между собой
    Map<task5.task1.ui.menu.MenuId, Menu> createMenus();
}