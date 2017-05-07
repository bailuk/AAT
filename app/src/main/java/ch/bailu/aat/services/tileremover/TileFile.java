package ch.bailu.aat.services.tileremover;

import java.io.File;

public class TileFile {

    private final short zoom;
    private final int x, y, source;
    private final long age;
    private final long size;


    public static int getX(File file)throws NumberFormatException {
        return Integer.valueOf(file.getName());
    }


    public static short getZoom(File file) throws NumberFormatException {
        return Short.valueOf(file.getName());
    }


    public static int getY(File file) throws NumberFormatException {
        final String name = file.getName();
        final String yname = name.substring(0, name.length()-4);

        return Integer.valueOf(yname);
    }


    public static TileFile toTileFile(File file, int source) {

        try {
            File pX = file.getParentFile();

            if (pX != null) {
                File pZoom = pX.getParentFile();

                if (pZoom != null) {
                    int x = getX(pX);
                    short zoom = getZoom(pZoom);

                    return new TileFile(source, zoom, x, file);
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;

    }

    public TileFile(int summary, short zoom, int x, int y, File file) {
        this.source = summary;
        this.zoom = zoom;
        this.x = x;
        this.y = y;
        age = file.lastModified();
        size = file.length();
    }

    public TileFile(int summary, short zoom, int x, File file) {
        this(summary, zoom, x, getY(file), file);
    }


    public File toFile(File base_dir) {
        return new File(base_dir, toString());
    }

    @Override
    public String toString() {
        return String.valueOf(zoom)+"/" +
                String.valueOf(x) +
                "/" +
                String.valueOf(y)+ ".png";
    }
    public long lastModified() {
        return age;
    }

    public long length() {
        return size;
    }


    public int getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        if (o instanceof TileFile) {
            return (((TileFile) o).x == x &&
                    ((TileFile) o).y == y &&
                    ((TileFile) o).zoom == zoom &&
                    ((TileFile) o).source == source
            );
        }
        return false;
    }
}
