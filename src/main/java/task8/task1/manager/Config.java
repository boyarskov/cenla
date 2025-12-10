package task8.task1.manager;

import task8.task1.annotations.config.ConfigProperty;
import task8.task1.framework.config.AnnotationConfigLoader;

public class Config {
    @ConfigProperty
    private boolean allowStatusChange = true;

    @ConfigProperty
    private int maxGuestHistoryEntries = 3;

    public Config(String fileName) {
        AnnotationConfigLoader loader = new AnnotationConfigLoader();
        loader.configure(this);
    }

    public boolean isAllowStatusChange() {
        return allowStatusChange;
    }

    public int getMaxGuestHistoryEntries() {
        return maxGuestHistoryEntries;
    }
}