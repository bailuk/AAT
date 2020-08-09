package ch.bailu.aat.activities;

import ch.bailu.aat.util.WithStatusText;

public class AppThread implements WithStatusText {

    /*
     * threads:
     * - CrAsyncTask or AsyncTask and Chrome_ProcessLauncher come from
     *   HtmlTextView.enableAutoLink()
     * - LayerManager (MapsForge) does not always terminate
     * - Animator (MapsForge) does not always terminate
     */

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
