package task8.task1.framework.di;

import task8.task1.annotations.di.Inject;

import java.lang.reflect.Field;
import java.util.Map;

public class SimpleInjector {
    public void injectDependencies(Object target, Map<Class<?>, Object> instances) {
        if (target == null || instances == null) {
            return;
        }

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?> fieldType = field.getType();

            Object dependency = instances.get(fieldType);
            if (dependency == null) {
                throw new IllegalStateException(
                        "Не найден объект для внедрения в поле "
                                + field.getName() + " типа " + fieldType.getName()
                );
            }

            boolean accessible = field.canAccess(target);
            field.setAccessible(true);
            try {
                field.set(target, dependency);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "Не удалось внедрить зависимость в поле " + field.getName(), e
                );
            } finally {
                field.setAccessible(accessible);
            }
        }
    }
}