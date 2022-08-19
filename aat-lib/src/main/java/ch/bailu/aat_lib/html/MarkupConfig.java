package ch.bailu.aat_lib.html;

public interface MarkupConfig {
    MarkupConfig HTML = new MarkupConfig() {
        @Override
        public String getNewLine() {
            return "<br>";
        }

        @Override
        public String getBoldOpen() {
            return "<b>";
        }

        @Override
        public String getBoldClose() {
            return "</b>";
        }

        @Override
        public String getBigOpen() {
            return "<h3>";
        }

        @Override
        public String getBigClose() {
            return "</h3>";
        }
    };

    String getNewLine();
    String getBoldOpen();
    String getBoldClose();
    String getBigOpen();
    String getBigClose();
}
