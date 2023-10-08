package ch.bailu.aat_lib.preferences;

public interface StorageInterface {
    String readString(String key);
    void writeString(String key, String value);
    int readInteger(String key);
    void writeInteger(String key, int v);
    void writeIntegerForce(String key, int v);

    long readLong(String key);
    void writeLong(String key, long v);

    void register(OnPreferencesChanged listener);
    void unregister(OnPreferencesChanged l);

    boolean isDefaultString(String s);
    String getDefaultString();
}
