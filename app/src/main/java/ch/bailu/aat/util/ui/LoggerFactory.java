package ch.bailu.aat.util.ui;


import ch.bailu.aat.util.ui.Logger;

public class LoggerFactory {

	public static Logger getLogger(Class<?> class1) {
		return new Logger(class1);
	}

}
