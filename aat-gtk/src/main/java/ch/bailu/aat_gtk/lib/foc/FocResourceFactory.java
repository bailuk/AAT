package ch.bailu.aat_gtk.lib.foc;

import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public class FocResourceFactory implements FocFactory {
        @Override
        public Foc toFoc(String string) {
            return new FocResource(string);
        }
}
