package ch.bailu.aat_awt.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SL4JCache {
    private final Map<String, Logger> loggers = new HashMap<>();

    public static final SL4JCache SELF = new SL4JCache();

    private SL4JCache() {}


    public Logger get(Class c) {
        return get(c.getName());
    }

    public Logger get(String name) {
        Logger result;

        if (loggers.containsKey(name)) {
            result =  loggers.get(name);
        } else {
            result = LoggerFactory.getLogger(name);
            loggers.put(name, result);
        }

        return result;
    }
}
