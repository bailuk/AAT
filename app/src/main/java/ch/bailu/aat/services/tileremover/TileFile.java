package ch.bailu.aat.services.tileremover;

import java.io.File;

public class TileFile {

    private final short zoom;
    private final int x, y, hash;
    private final long age;
    private final long size;


    public static int getBaseDirHash(File file) {
        return file.getName().hashCode();
    }


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


    public TileFile(int hash, short zoom, int x, File file) {
        this.hash = hash;
        this.zoom = zoom;
        this.x = x;
        this.y = getY(file);
        this.age = file.lastModified();
        this.size = file.length();
    }


    public File toFile(File base_dir) {
        return new File(base_dir,
                String.valueOf(zoom)+"/" +
                        String.valueOf(x) +
                        "/" +
                        String.valueOf(y)+ ".png");
    }

    public long lastModified() {
        return age;
    }

    public long length() {
        return size;
    }


    public int directoryHashCode() {
        return hash;
    }
}
