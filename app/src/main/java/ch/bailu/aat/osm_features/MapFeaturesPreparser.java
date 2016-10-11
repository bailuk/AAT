package ch.bailu.aat.osm_features;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import ch.bailu.aat.gpx.parser.SimpleStream;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.services.background.DownloadHandle;


public class MapFeaturesPreparser {
    private static final int TARGET_LENGTH=30;
    private static final String WIKI_URL = "http://wiki.openstreetmap.org/";

    private final static boolean ENABLE_LINKS = false;

    private boolean enableImages = false;

    
    private final ArrayList<DownloadHandle> images = new ArrayList<>();
    
    
    private final StringBuilder builder = new StringBuilder();
    private final StringBuilder targetBuilder = new StringBuilder();
    
    
    private final SimpleStream  in;
    private BufferedWriter out=null;
    
    private int tableDataCount=0;
    
    


    
    
    public MapFeaturesPreparser(Context c) throws IOException {
        in = new SimpleStream(new FileAccess(AppDirectory.getMapFeatureIndex(c)));
        
        parseMapFeatures(c);
        AppBroadcaster.broadcast(c,
                AppBroadcaster.FILE_CHANGED_ONDISK,
                AppDirectory.getDataDirectory(c, AppDirectory.DIR_OSM_FEATURES_PREPARSED).toString(),
                AppDirectory.getMapFeatureIndex(c).toString()
                );

        
        closeOut();
        in.close();
    }
    
    
    private void openOut(Context c, String name) throws SecurityException, IOException {
        File file = new File(
                AppDirectory.getDataDirectory(c, AppDirectory.DIR_OSM_FEATURES_PREPARSED), 
                name
                );
        
        closeOut();
        
        Writer ostream = new FileWriter(file);
        out = new BufferedWriter(ostream);
    }
    
    
    private void closeOut() throws IOException {
        if (out != null) {
            out.close();
            out=null;
        }
    }
    
    
    private void parseMapFeatures(Context c) throws IOException {
        
        while (true) {
            in.to('<');
            
            if (in.haveEOF()) {
                break;
                
            } else if (in.nextIs("h3")) { // <h3>
                parsePrimaryFeature(c);
            }
        }
    }

    
    private void parsePrimaryFeature(Context c) throws IOException {
        
        while (true) {
            in.to('<');
            in.read();
            
            if (in.haveEOF()) {
                break;
            
            
            } else if (in.haveA('s') && in.nextIs("pan")) { // <spand class ...>
                in.to('>');
                parseName();
                if (builder.length()>0) {
                    startNewList(c);
                }
                
                
            } else  if (in.haveA('p') && in.nextIs('>') && out != null) { //<p>
                //ENABLE_LINKS=true;
                
                out.append("\n<p>");
                parseParagraph(c);
                out.append("</p>\n");
                
                //ENABLE_LINKS=false;
                
            } else if (in.haveA('t') && in.nextIs("able")&& out != null) { // <table ...
                parseTable(c);
                break;
            }
        }
    }

    private void startNewList(Context c) throws SecurityException, IOException {
        String name = builder.toString();

        openOut(c, name);
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




    private void parseTable(Context c) throws IOException {
        while (true) {
            in.to('<');
            in.read();
            
            if (in.haveEOF()) {
                break;
            } else if (in.haveA('t')) { // <tr>
                if (in.nextIs('r')) {
                    parseTableRow(c, in);
                }
            } else if (in.haveA('/')) { // </table>
                if (in.nextIs("table")) {
                    break;
                }
            }
        }
    }

    private void parseTableRow(Context c, SimpleStream in) throws IOException {
        while(true) {
            in.to('<');
            in.read();
            
            if (in.haveEOF()) {
                break;
            } else if (in.haveA('t') && in.nextIs("d>")) { // <td>
                parseTableData(c); 
                
            } else if (in.haveA('/') && in.nextIs("tr>")) { //</tr>
                break;
            }
        }
        
        if (tableDataCount>0) {
            out.append("</p>\n");
            tableDataCount=0;
        }
    }

    
    private void parseTableData(Context c) throws IOException {
        if (tableDataCount==0) {
            enableImages =true;
            
            out.append("\n<p>[<b>");
            parseParagraph(c);
            out.append("</b>");
            
        } else if (tableDataCount == 1) {
            out.append("=<b>");
            parseParagraph(c);
            out.append("</b>]");
        } else {
            
            if (tableDataCount == 4) {
                enableImages =false;
            }
            parseParagraph(c);
        }
        
        tableDataCount++;
    }


    /**
     * <td> ... </td>
     * 
     */
    private void parseParagraph(Context c) throws IOException {
        
        
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
                    parseImage(c);
                    
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

    private void parseImage(Context c) throws IOException {
        /**
         *  <img alt="Node" src="/w/images/b/b5/Mf_node.png" width="20" height="20" />
         */

        while(true) {
            in.to('s');
            
            if (in.nextIs("rc=")) {
                
                if (enableImages) {
                    String source = parseQuotedUrl();
                    String target = generateTargetName(c,source);

                    out.append("<img src=\"");
                    out.append(target);
                    out.append("\"/>");
                
                    addImage(source, target);
                }
                break;
                
            } else if (in.haveEOF()) {
                break;
            }
        }
        
    }




    private String generateTargetName(Context context, String source) throws SecurityException, IOException {
        int start=0;
        if (source.length()>TARGET_LENGTH) {
            start=source.length()-TARGET_LENGTH;
        }

        targetBuilder.setLength(0);
        targetBuilder.append(AppDirectory.getDataDirectory(context, AppDirectory.DIR_OSM_FEATURES_IMAGES));
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


    private void addImage(String source, String target) {
        final File file = new File(target);

        for (int i=0; i<images.size(); i++) {
            if (images.get(i).toString().equals(source)) return;
        }

        DownloadHandle handle = new DownloadHandle(source, file);
        images.add(handle);
            
        
    }


    private void parseLink() throws IOException {
        /**
         *  <a href="/wiki/Elements#Node" title="Node">
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


    public ArrayList<DownloadHandle> getImageList() {
        return images;
    }
    
}
