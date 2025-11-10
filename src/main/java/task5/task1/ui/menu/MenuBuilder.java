package task5.task1.ui.menu;

public class MenuBuilder {
    private Menu root;

    public MenuBuilder build(Menu rootMenu) {
        this.root = rootMenu;
        return this;
    }

    public Menu getRootMenu() {
        return root;
    }
}
