package task8.task1.ui.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private String name;
    private final List<MenuItem> items = new ArrayList<>();

    public Menu(String name) { this.name = name; }

    public String getName() { return name; }
    public List<MenuItem> getItems() { return Collections.unmodifiableList(items); }

    public Menu addItem(MenuItem item) {
        items.add(item);
        return this;
    }
}
