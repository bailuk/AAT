package ch.bailu.util_java.parser;

import java.io.IOException;


public interface OnParsedInterface {
    public void onHaveSegment();
    public void onHavePoint() throws IOException;
    
    
    public final static OnParsedInterface NULL = new OnParsedInterface() {
		@Override
		public void onHaveSegment() {}
		@Override
		public void onHavePoint() {}
	};
}
