package ch.bailu.aat.helpers;


public class LoggerFactory {

	public static Logger getLogger(Class<?> class1) {
		return new Logger(class1);
	}

}
