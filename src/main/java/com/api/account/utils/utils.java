package com.api.account.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class utils {

    public static Map<String, Object> getNonNullFields(Object obj) throws IllegalAccessException {
        Map<String, Object> nonNullFields = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                nonNullFields.put(field.getName(), value);
            }
        }

        return nonNullFields;
    }

    public static StringJoiner allowedFieldsValidator(Object obj, Set<String> fieldsNotAllowed) throws IllegalArgumentException, IllegalAccessException {

        StringJoiner fields = new StringJoiner(", ");

        Field[] declaredFields = obj.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value!=null && fieldsNotAllowed.contains(field.getName())) {
                fields.add(field.getName());
            }
        }

        return fields;

    }
}