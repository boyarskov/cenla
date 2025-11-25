package task6.task1.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private boolean allowStatusChange = true;
    private int maxGuestHistoryEntries = 3;

    public Config(String fileName) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);

            String allowStr = properties.getProperty("allowStatusChange");
            if (allowStr != null) {
                allowStatusChange = Boolean.parseBoolean(allowStr);
            }

            String historyStr = properties.getProperty("maxGuestHistoryEntries");
            if (historyStr != null) {
                maxGuestHistoryEntries = Integer.parseInt(historyStr);
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл настроек: " + fileName);
            System.out.println("Будут использованы значения по умолчанию.");
            }
        }

    public boolean isAllowStatusChange() {
        return allowStatusChange;
    }

    public int getMaxGuestHistoryEntries() {
        return maxGuestHistoryEntries;
    }
}