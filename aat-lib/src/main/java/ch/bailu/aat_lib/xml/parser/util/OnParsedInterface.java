package ch.bailu.aat_lib.xml.parser.util;

import java.io.IOException;


public interface OnParsedInterface {
    void onHaveSegment();
    void onHavePoint() throws IOException;
    
    
    OnParsedInterface NULL = new OnParsedInterface() {
		@Override
		public void onHaveSegment() {}
		@Override
		public void onHavePoint() {}
	};
}
