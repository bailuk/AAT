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


    public TileFile(int summary, short zoom, int x, File file) {
        this.source = summary;
        this.zoom = zoom;
        this.x = x;
        this.y = getY(file);
        this.age = file.lastModified();
        this.size = file.length();
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
