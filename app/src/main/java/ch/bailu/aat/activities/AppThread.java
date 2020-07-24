package ch.bailu.aat.activities;

import ch.bailu.aat.util.WithStatusText;

public class AppThread implements WithStatusText {

    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("<h1>").append(getClass().getSimpleName()).append("</h1>");
        builder.append("<p>");

        Thread[] threads = new Thread[Thread.activeCount()+5];
        int count = Thread.enumerate(threads);

        for (int i = 0; i<count; i++) {
            builder .append(threads[i].getId())
                    .append(": ")
                    .append(threads[i].getName())
                    .append(", ")
                    .append(threads[i].getState())
                    .append("<br>");
        }
        builder.append("</p>");
    }
}
