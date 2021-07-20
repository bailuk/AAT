package ch.bailu.aat_lib.xml.parser.util;


import java.io.IOException;

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
