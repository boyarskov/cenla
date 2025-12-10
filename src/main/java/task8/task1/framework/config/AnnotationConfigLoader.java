package task8.task1.framework.config;

import task8.task1.annotations.config.ConfigProperty;
import task8.task1.annotations.config.ConfigValueType;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class AnnotationConfigLoader {
    public void configure(Object target) {
        if (target == null) {
            return;
        }

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation == null) {
                continue;
            }

            String configFileName = annotation.configFileName();
            if (configFileName == null || configFileName.isBlank()) {
                configFileName = "config.properties";
            }

            Properties properties = loadProperties(configFileName);
            if (properties == null) {
                continue;
            }

            String propertyName = annotation.propertyName();
            if (propertyName == null || propertyName.isBlank()) {
                propertyName = clazz.getSimpleName() + "." + field.getName();
            }

            String rawValue = properties.getProperty(propertyName);
            if (rawValue == null) {
                continue;
            }

            rawValue = rawValue.trim();

            Object convertedValue = convertValue(rawValue, field.getType(), annotation.type());
            if (convertedValue == null) {
                continue;
            }

            boolean accessible = field.canAccess(target);
            field.setAccessible(true);
            try {
                field.set(target, convertedValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Не удалось установить значение в поле " + field.getName(), e);
            } finally {
                field.setAccessible(accessible);
            }
        }
    }

    private Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
            return props;
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл настроек: " + fileName);
            return null;
        }
    }

    private Object convertValue(String value, Class<?> fieldType, ConfigValueType configType) {
        if (configType != ConfigValueType.AUTO) {
            switch (configType) {
                case STRING:
                    return value;
                case INT:
                    return Integer.parseInt(value);
                case BOOLEAN:
                    return Boolean.parseBoolean(value);
                default:
                    break;
            }
        }

        if (fieldType == String.class) {
            return value;
        }
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        }
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}