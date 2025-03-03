package test.config;

import java.util.HashMap;

/**
 * @author ke
 */
public class RequestContext {

    private static final ThreadLocal<HashMap<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void put(String key, Object value) {
        HashMap<String, Object> objectMap = CONTEXT.get();
        if (objectMap == null) {
            objectMap = new HashMap<>();
        }
        objectMap.put(key, value);
        CONTEXT.set(objectMap);
    }

    public static Object get(String key) {
        HashMap<String, Object> objectMap = CONTEXT.get();
        return objectMap == null ? null : objectMap.get(key);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}