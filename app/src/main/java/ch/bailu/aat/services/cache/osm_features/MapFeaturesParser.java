package ch.bailu.aat.services.cache.osm_features;

import android.content.res.AssetManager;

import java.io.IOException;

import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.io.Stream;

public final class MapFeaturesParser {
    private static final String MAP_FEATURES_ASSET = "map_features";

    private final OnHaveFeature haveFeature;

    private final StringBuilder out = new StringBuilder();

    private final StringBuilder outName = new StringBuilder();
    private final StringBuilder outKey = new StringBuilder();
    private final StringBuilder outValue = new StringBuilder();

    private String outSummarySearchKey = "";
    private String outSummaryKey = "";

    private int id = 0;

    public MapFeaturesParser(AssetManager assets, OnHaveFeature hf) throws IOException {
        this(assets, hf, assets.list(MAP_FEATURES_ASSET));
    }

    public MapFeaturesParser(AssetManager assets,
                             OnHaveFeature hf,
                             String[] files) throws IOException {
        haveFeature = hf;

        for (String f : files) {
            if (haveFeature.onParseFile(f)) {
                Foc file = new FocAsset(assets, MAP_FEATURES_ASSET + "/" + f);
                parseFeatures(file);
            }


        }
    }


    public String getSummarySearchKey() {
        return outSummarySearchKey;
    }

    public String getSumaryKey() {
        return outSummaryKey;
    }


    public int getId() {
        return id;
    }


    public interface OnHaveFeature {
        boolean onParseFile(String file);
        void onHaveFeature(MapFeaturesParser parser);
    }





    public StringBuilder addHtml(StringBuilder b) {
        b.append(out);
        return b;
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



    private void parseFeatures(Foc file) throws IOException {
        Stream in = new Stream(file);

        parseSummary(in);
        haveSummary();

        while(!in.haveEOF()) {
            parseFeature(in);
            haveFeature();
        }

        in.close();
    }



    private void parseFeature(Stream in) throws IOException {
        parseKeyValue(in);
        parseToEndOfParagraph(in);
    }


    private void parseKeyValue(Stream in) throws IOException {
        // <b><a ...>key</a></b>=<a ...>value</a>
        parseBoldName(in, outKey);
        parseBoldName(in, outValue);
    }


    private void parseBoldName(Stream in, StringBuilder outString) throws IOException {
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


    private void parseName(Stream in, StringBuilder outString) throws IOException {
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


    private void parseString(Stream in, StringBuilder outString) throws IOException {

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


    private void parseSummary(Stream in) throws IOException {
        parseSummaryHeading(in);
        parseToEndOfParagraph(in);

    }


    private void haveSummary() {

        outSummaryKey = outName.toString().toLowerCase();
        outSummarySearchKey = "_" + outSummaryKey;


        haveFeature.onHaveFeature(this);
        resetFeature();
    }

    private void haveFeature() {
        if (outKey.length() > 0)
            haveFeature.onHaveFeature(this);

        resetFeature();
    }


    private void resetFeature() {
        id++;
        out.setLength(0);
        outKey.setLength(0);
        outValue.setLength(0);
        outName.setLength(0);
    }


    private void parseToEndOfParagraph(Stream in) throws IOException {
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


    private void parseSummaryHeading(Stream in) throws IOException {
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
