package ch.bailu.util_java.parser.scanner;
import java.io.IOException;

import ch.bailu.util_java.io.Stream;


public class IntegerScanner extends AbsScanner {
	private int integer;


	public void scan(Stream stream) throws IOException {
		integer=0;
		
		stream.read();
		stream.skipWhitespace();
		
		while (true) {
			if (stream.haveDigit()) {
				integer*=10;
				integer+=stream.getDigit();
			} else {
				break;
			}
			stream.read();
		}
		
	}
	
	public int getInteger() {
		return integer;
	}
}
