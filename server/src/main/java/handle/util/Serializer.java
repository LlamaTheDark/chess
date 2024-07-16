package handle.util;

import com.google.gson.*;

import java.util.Calendar;
import java.util.Map;

public class Serializer {
    static Gson gson = new Gson();

    static public String serialize(Object obj){
        return gson.toJson(obj);
    }

    static public <T> T deserialize(String obj, Class<T> clazz) {
        return gson.fromJson(obj, clazz);
    }
}
