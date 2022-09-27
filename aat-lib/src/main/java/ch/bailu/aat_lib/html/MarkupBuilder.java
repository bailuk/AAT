package ch.bailu.aat_lib.html;


import javax.annotation.Nonnull;

public class MarkupBuilder {

    private final StringBuilder b = new StringBuilder();

    protected final MarkupConfig config;

    public MarkupBuilder(MarkupConfig config) {
        this.config = config;
    }

    public void clear() {
        b.setLength(0);
    }

    public void appendHeader(String s) {
        append(config.getBigOpen());
        append(s);
        append(config.getBigClose());
    }

    public void append(String s) {
        b.append(s);
    }

    public void appendNl(String s) {
        b.append(s);
        b.append(config.getNewLine());
    }

    public void appendBoldNl(String k, String v) {
        append(config.getBoldOpen());
        append(k);
        append("=");
        append(v);
        append(config.getBoldClose());
        append(config.getNewLine());
    }

    public void appendKeyValueNl(String k, String v) {
        append(k);
        append("=");
        append(v);
        append(config.getNewLine());
    }

    public void appendNl(String l, String v) {
        append(l);
        append(": ");
        append(v);
        append(config.getNewLine());
    }

    @Nonnull
    @Override
    public String toString() {
        return b.toString();
    }
}
