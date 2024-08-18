package by.aston.bank.utils;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonHelper {

    private static final Gson GSON = new Gson();

    public static <E> String writeAsJson(E object) {
        return GSON.toJson(object);
    }

    public static <E> E readFromJson(String json, Class<E> clazz) {
        return GSON.fromJson(json, clazz);
    }

}