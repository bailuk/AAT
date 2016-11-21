package ch.bailu.aat.helpers;

public class HtmlBuilder {

    private final StringBuilder b = new StringBuilder();


    public void clear() {
        b.setLength(0);
    }


    public void appendHeader(String s) {
        append("<h3>");
        append(s);
        append("</h3>");
    }


    public void append(String s) {
        b.append(s);
    }

    public void appendKeyValueBold(String k, String v) {
        append("<li><b>");
        append(k);
        append("=");
        append(v);
        append("</b></li>");
    }

    public void appendKeyValue(String k, String v) {
        append("<li>");
        append(k);
        append("=");
        append(v);
        append("</li>");
    }

    public void append(String l, String v) {
        append(l);
        append(": ");
        append(v);
    }


    @Override
    public String toString() {
        return b.toString();
    }

}
