package ch.bailu.aat_lib.logger;

public class PrintLnLoggerFactory implements LoggerFactory {
    @Override
    public Logger warn() {
        return new PrintLnLogger("WARN");
    }

    @Override
    public Logger info() {
        return new PrintLnLogger("INFO");
    }

    @Override
    public Logger debug() {
        return new PrintLnLogger("DEBUG");
    }

    @Override
    public Logger error() {
        return new PrintLnLogger("ERROR", System.err);
    }
}
