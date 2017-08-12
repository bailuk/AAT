package ch.bailu.aat.util;

import java.io.UnsupportedEncodingException;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;


public abstract class OsmApiHelper {

        public final static int NAME_MAX=15;
        public final static int NAME_MIN=2;


        public abstract String getApiName();
        public abstract String getUrl(String query) throws UnsupportedEncodingException;
        public abstract String getUrlEnd();
        public abstract String getUrlStart();
        public abstract Foc getBaseDirectory();
        public abstract String getFileExtension();

        public Foc getResultFile() {
            return getBaseDirectory().child("result"+ getFileExtension());
        }
        

        public Foc getQueryFile() {
            return getBaseDirectory().child("query.txt");
        }

        public static String getFilePrefix(String query) {
            final StringBuilder name= new StringBuilder();
            
            for (int i=0; i<query.length() && name.length()<NAME_MAX; i++) {
                appendToName(query.charAt(i), name);
            }
            
            if (name.length()<NAME_MIN) {
                name.append(AppDirectory.generateDatePrefix());
            }
            
            return name.toString();
        }


        private static void appendToName(char c, StringBuilder name) {
            if (Character.isLetter(c)) {
                name.append(c);
            }
        }


    }


