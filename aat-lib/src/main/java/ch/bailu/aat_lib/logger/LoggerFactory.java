package ch.bailu.aat_lib.logger;

public interface LoggerFactory {
    Logger warn();
    Logger info();
    Logger debug();
    Logger error();
}
