package ch.bailu.aat.osm_features;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.parser.SimpleStream;
import ch.bailu.aat.helpers.file.FileAccess;

public class MapFeaturesParser {
    
    private final OnHaveFeature haveFeature;
    
    private final StringBuilder out=new StringBuilder();
    private final StringBuilder outName=new StringBuilder();
    private final StringBuilder outKey=new StringBuilder();
    private final StringBuilder outValue=new StringBuilder();
    
    
    
    public interface OnHaveFeature {
        void onHaveFeature(MapFeaturesParser parser);
    }
    
    
    public MapFeaturesParser(OnHaveFeature hf, FileAccess file) throws IOException {
        haveFeature = hf;
        
        parseFeatures(file);
    }
    

    public MapFeaturesParser(OnHaveFeature hf, File[] files) throws IOException {
        haveFeature = hf;


        for (File file : files) {
            if (file.exists() && file.canRead() && file.isFile()) {
                parseSummary(new FileAccess(file));
            }
        }
    }
    


    public void toHtml(StringBuilder html) {
        html.append(out);
    }
    
    
    public String getName() {
        return outName.toString();
    }
    
    public String getKey() {
        return outKey.toString();
    }
    
    public String getValue() {
        return outValue.toString();
    }
    

    

    
    private void parseSummary(FileAccess file) throws IOException {
        SimpleStream in = new SimpleStream(file);
        
        parseSummary(in);
        haveFeature();
        
        in.close();
    }
    
    
    private void parseFeatures(FileAccess file) throws IOException {
        SimpleStream in = new SimpleStream(file); 

        parseSummary(in);
        resetFeature();
        
        while(!in.haveEOF()) {
            parseFeature(in);
            haveFeature();
        }
        
        in.close();
    }

    
    
    
    private void parseFeature(SimpleStream in) throws IOException {
        parseKeyValue(in);
        parseToEndOfParagraph(in);
    }


    private void parseKeyValue(SimpleStream in) throws IOException {
        // <b><a ...>key</a></b>=<a ...>value</a>
        parseBoldName(in, outKey);
        parseBoldName(in, outValue);
    }


    private void parseBoldName(SimpleStream in, StringBuilder outString) throws IOException {
        int state=0;
        
        while(state<4) {
            in.read();
            
            if (in.haveEOF()) {
                break;
            } else if (in.haveA('<')) {
                state++;
            } else if (in.haveA('b') && state==1) {
                state++;
            } else if (in.haveA('>') && state==2) {
                state++;
            } else if (state==3) {
                parseName(in, outString);
                state++;
            } else {
                state=0;
            }
            
            out.append((char)in.get());
        }
    }


    private void parseName(SimpleStream in, StringBuilder outString) throws IOException {
        int state=0;
        int lock=0;
        
        while(state < 3) {
            
            if (in.haveEOF()) {
                break;
                
            } else if (in.haveA('<') && state==0) {
                state++;
                lock++;
                
            } else if (in.haveA('/') && state==1) {
                state++;
                
            } else if (in.haveA('b') && state==2) {
                state++;
                
            } else {
                if (in.haveA('>')) {
                    lock--;
                }
                state=0;
            }

            if (lock < 1 && !in.haveA('>') )
                outString.append((char)in.get());
            
            out.append((char)in.get());
            
            in.read();
        }
    }


    private void parseString(SimpleStream in, StringBuilder outString) throws IOException {
        
        while (true) {
            if (in.haveEOF() || in.haveA('<')) {
                break;
                
            } else if (in.haveCharacter()) {
                outString.append((char)in.get());
            }
            
            out.append((char)in.get());
            
            in.read();
        }
    }


    private void parseSummary(SimpleStream in) throws IOException {
        parseSummaryHeading(in);
        parseToEndOfParagraph(in);
        
    }

    
    private void haveFeature() {
        
        if (out.length()>1) {
            haveFeature.onHaveFeature(this);
        }
        resetFeature();
    }
    
    
    private void resetFeature() {
        out.setLength(0);
        outKey.setLength(0);
        outValue.setLength(0);
        outName.setLength(0);
    }
    
    
    private void parseToEndOfParagraph(SimpleStream in) throws IOException {
        int state=0;
        
        while(state < 4) {
            in.read();
            
            if (in.haveEOF()) {
                break;
                
            } else if (in.haveA('<') && state==0) {
                state++;
                
            } else if (in.haveA('/') && state==1) {
                state++;
                
            } else if (in.haveA('p') && state==2) {
                state++;
                
            } else if (in.haveA('>') && state == 3) {
                state++;
                
            } else {
                state=0;
                
            }
            
            out.append((char)in.get());
        }
    }


    private void parseSummaryHeading(SimpleStream in) throws IOException {
        int state=0;
        
        while(state<2) {
            in.read();
            
            if (in.haveEOF()) {
                break;
                
            } else if (in.haveA('>') ) {
                state=1;
            } else if (state == 1) {
                parseString(in, outName);
                state=2;
            }
            
            out.append((char)in.get());
        }
    }
}
