package ch.bailu.util_java.parser.scanner;
import java.io.IOException;

import ch.bailu.util_java.io.Stream;


public class IntegerScanner {
	private int integer;
	private final Stream stream;
	
	public IntegerScanner(Stream s) {
		stream = s;
	}
	
	
	public void scan() throws IOException {
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
