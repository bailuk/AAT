package ch.bailu.util_java.parser;

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
