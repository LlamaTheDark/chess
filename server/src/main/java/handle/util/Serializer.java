package handle.util;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;

/**
 * Serializes and deserializes JSON objects using <a href="https://github.com/google/gson">Google's Gson Library</a>.
 */
public
class Serializer {
    static Gson gson = new Gson();

    /**
     * Serializes an object.
     *
     * @param obj The object to be serialized.
     *
     * @return A <code>String</code> containing the serialized object.
     */
    static public
    String serialize(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Deserializes an object.
     *
     * @param obj   The String containing the serialized object.
     * @param clazz The class of the object to
     * @param <T>   The type variable which is of the same type as the class.
     *
     * @return The deserialized object, or a new instance of the object if <code>obj</code> is <code>null</code>.
     */
    static public
    <T> T deserialize(String obj, Class<T> clazz)
    throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var deserialized = gson.fromJson(obj, clazz);

        if (deserialized == null) {
            deserialized = clazz.getDeclaredConstructor().newInstance();
        }

        return deserialized;
    }
}
