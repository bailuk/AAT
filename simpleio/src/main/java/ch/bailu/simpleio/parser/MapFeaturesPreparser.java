package ch.bailu.simpleio.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;
import ch.bailu.simpleio.io.Stream;

public class MapFeaturesPreparser {
    private static final int TARGET_LENGTH=30;
    private static final String WIKI_URL = "http://wiki.openstreetmap.org/";

    private final static boolean ENABLE_LINKS = false;

    private boolean enableImages = false;



    private final StringBuilder builder = new StringBuilder();
    private final StringBuilder targetBuilder = new StringBuilder();


    private final Stream in;
    private BufferedWriter out=null;

    private int tableDataCount=0;



    private final Foc outDir, imageDir;

    public static void main(String [] args) {
        if (args.length > 2) {

            try {
                new MapFeaturesPreparser(
                        new FocFile(args[0]),
                        new FocFile (args[1]),
                        new FocFile (args[2]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("Usage: MapFeaturesPreparser 'input_file' 'output_directory' 'image_directory'\n");
        }

    }

    public MapFeaturesPreparser(Foc in, Foc outDir, Foc imageDir) throws IOException {
        this.in = new Stream(in);
        this.outDir = outDir;
        this.imageDir = imageDir;
        parseMapFeatures();

        closeOut();
        this.in.close();
    }


    private void openOut(String name) throws SecurityException, IOException {
        Foc file = outDir.child(name);

        closeOut();

        out = new BufferedWriter(new OutputStreamWriter(file.openW()));
    }


    private void closeOut() throws IOException {
        if (out != null) {
            out.close();
            out=null;
        }
    }


    private void parseMapFeatures() throws IOException {

        while (true) {
            in.to('<');

            if (in.haveEOF()) {
                break;

            } else if (in.nextIs("h3")) { // <h3>
                parsePrimaryFeature();
            }
        }
    }


    private void parsePrimaryFeature() throws IOException {

        while (true) {
            in.to('<');
            in.read();

            if (in.haveEOF()) {
                break;


            } else if (in.haveA('s') && in.nextIs("pan")) { // <spand class ...>
                in.to('>');
                parseName();
                if (builder.length()>0) {
                    startNewList();
                }


            } else  if (in.haveA('p') && in.nextIs('>') && out != null) { //<p>
                //ENABLE_LINKS=true;

                out.append("\n<p>");
                parseParagraph();
                out.append("</p>\n");

                //ENABLE_LINKS=false;

            } else if (in.haveA('t') && in.nextIs("able")&& out != null) { // <table ...
                parseTable();
                break;
            }
        }
    }

    private void startNewList() throws SecurityException, IOException {
        String name = builder.toString();

        openOut(name);
        out.append("<h1>");
        out.append(name);
        out.append("</h1>\n");
        builder.setLength(0);
    }


    private void parseName() throws IOException {
        builder.setLength(0);

        while(true) {
            in.read();

            if (in.haveA('<') || in.haveA('>') || in.haveEOF()) {
                break;

            } else if (in.haveCharacter()) {
                builder.append((char)in.get());
            }
        }

    }




    private void parseTable() throws IOException {
        while (true) {
            in.to('<');
            in.read();

            if (in.haveEOF()) {
                break;
            } else if (in.haveA('t')) { // <tr>
                if (in.nextIs('r')) {
                    parseTableRow(in);
                }
            } else if (in.haveA('/')) { // </table>
                if (in.nextIs("table")) {
                    break;
                }
            }
        }
    }

    private void parseTableRow(Stream in) throws IOException {
        while(true) {
            in.to('<');
            in.read();

            if (in.haveEOF()) {
                break;
            } else if (in.haveA('t') && in.nextIs("d>")) { // <td>
                parseTableData();

            } else if (in.haveA('/') && in.nextIs("tr>")) { //</tr>
                break;
            }
        }

        if (tableDataCount>0) {
            out.append("</p>\n");
            tableDataCount=0;
        }
    }


    private void parseTableData() throws IOException {
        if (tableDataCount==0) {
            enableImages =true;

            out.append("\n<p>[<b>");
            parseParagraph();
            out.append("</b>");

        } else if (tableDataCount == 1) {
            out.append("=<b>");
            parseParagraph();
            out.append("</b>]");
        } else {

            if (tableDataCount == 4) {
                enableImages =false;
            }
            parseParagraph();
        }

        tableDataCount++;
    }


    /**
     * <td> ... </td>
     *
     */
    private void parseParagraph() throws IOException {


        boolean ignoreWS=true;

        in.read();
        while (true) {

            if (ignoreWS) {
                in.skipWhitespace();
            }


            if (in.haveEOF()) {
                break;

            } else if (in.haveA('<')) {
                in.read();
                if (in.haveA('a') && in.nextIs(' ')) { // <a href...
                    parseLink();

                } else if (in.haveA('i') && in.nextIs("mg ")) { // <img ...
                    parseImage();

                } else if (in.haveA('/')) {
                    in.read();
                    if (in.haveA('p') || in.haveA('t')) {
                        in.read();
                        if (in.haveA('>') || in.haveA('d')){
                            break;
                        }
                    } else if (in.haveA('a')) {
                        if (ENABLE_LINKS) out.append("</a>");
                    }

                }
                if (!in.haveA('>')) in.to('>');

                in.read();


            } else  {

                if (in.haveA('\n')==false)  {
                    out.append((char) in.get());
                    ignoreWS=false;
                }
                in.read();
            }

        }
    }

    private void parseImage() throws IOException {
        /*
           <img alt="Node" src="/w/images/b/b5/Mf_node.png" width="20" height="20" />
         */

        while(true) {
            in.to('s');

            if (in.nextIs("rc=")) {

                if (enableImages) {
                    String source = parseQuotedUrl();
                    String target = generateTargetName(source);

                    out.append("<img src=\"");
                    out.append(target);
                    out.append("\"/>");
                }
                break;

            } else if (in.haveEOF()) {
                break;
            }
        }

    }




    private String generateTargetName(String source) throws SecurityException, IOException {
        int start=0;
        if (source.length()>TARGET_LENGTH) {
            start=source.length()-TARGET_LENGTH;
        }

        targetBuilder.setLength(0);
        targetBuilder.append(imageDir);
        targetBuilder.append('/');

        for (int i=start; i<source.length(); i++) {
            char c = source.charAt(i);

            if (  ! ((c >= 'A' && c<='z')|| c=='.') ) {
                c = '_';
            }
            targetBuilder.append(c);
        }
        return targetBuilder.toString();
    }



    private void parseLink() throws IOException {
        /*
           <a href="/wiki/Elements#Node" title="Node">
         */

        while(true) {
            in.to('h');

            if (in.nextIs("ref=")) {
                if (ENABLE_LINKS) {
                    String url = parseQuotedUrl();
                    out.append("<a href=\"");
                    out.append(url);
                    out.append("\">");
                }
                break;

            } else if (in.haveEOF()) {
                break;
            }
        }
    }

    private String parseQuotedUrl() throws IOException {
        builder.setLength(0);

        in.to('"');
        while(true) {
            in.read();

            if (in.haveEOF()) {
                break;

            } else if (in.haveA('"')) {
                break;

            } else if (in.haveA('/') && builder.length()==0) {
                builder.append(WIKI_URL);

            } else if (!in.haveA('\t') && !in.haveA('\n')){
                builder.append((char)in.get());
            }
        }

        return builder.toString();
    }
}
