package ch.bailu.aat_lib.logger;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Logger {
    void log(@Nonnull String tag, @Nonnull String msg);
}
