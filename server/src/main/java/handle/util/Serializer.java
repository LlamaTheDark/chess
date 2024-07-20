package handle.util;

import com.google.gson.Gson;

public
class Serializer {
    static Gson gson = new Gson();

    static public
    String serialize(Object obj) {
        return gson.toJson(obj);
    }

    static public
    <T> T deserialize(String obj, Class<T> clazz) {
        var deserialized = gson.fromJson(obj, clazz);

        try {
            if (deserialized == null) {
                deserialized = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deserialized;
    }
}
