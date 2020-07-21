package ch.bailu.aat.services.tileremover;

import androidx.annotation.NonNull;

import ch.bailu.util_java.foc.Foc;

public final class TileFile {

    private final short zoom;
    private final int x, y, source;
    private final long age;
    private final long size;


    public static int getX(Foc file)throws NumberFormatException {
        return Integer.parseInt(file.getName());
    }


    public static short getZoom(Foc file) throws NumberFormatException {
        return Short.parseShort(file.getName());
    }


    public static int getY(Foc file) throws NumberFormatException {
        final String name = file.getName();
        final String yname = name.substring(0, name.length()-4);

        return Integer.parseInt(yname);
    }


    public static TileFile toTileFile(Foc file, int source) {

        try {
            Foc pX = file.parent();

            if (pX != null) {
                Foc pZoom = pX.parent();

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

    public TileFile(int summary, short zoom, int x, int y, Foc file) {
        this.source = summary;
        this.zoom = zoom;
        this.x = x;
        this.y = y;
        age = file.lastModified();
        size = file.length();
    }

    public TileFile(int summary, short zoom, int x, Foc file) {
        this(summary, zoom, x, getY(file), file);
    }


    public Foc toFile(Foc base_dir) {
        return base_dir.child(toString());
    }

    @NonNull
    @Override
    public String toString() {
        return zoom +"/" +
                x +
                "/" +
                y + ".png";
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
