package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.foc.Foc;

public abstract class TileScanner {

    private final Foc root;

    protected String source;
    protected short zoom;
    protected int x,y;
    protected String ext;

    public TileScanner(Foc r) {
        root = r;
    }

    public void scanZoomContainer() {
        source = root.getName();
        scanZoomContainer(root);
    }

    public void scanSourceContainer() {
        scanSourceContainer(root);
    }


    private void scanSourceContainer(Foc dir) {
        if (doDirectory(dir) && doSourceContainer(dir)) {
            dir.foreachDir(new Foc.OnHaveFoc() {
                @Override
                public void run(Foc child) {
                    source = child.getName();
                    scanZoomContainer(child);
                }
            });
        }
    }

    protected abstract boolean doSourceContainer(Foc dir);


    private void scanZoomContainer(final Foc dir) {
        if (doZoomContainer(dir)) {
            dir.foreachDir(new Foc.OnHaveFoc() {
                @Override
                public void run(Foc child) {
                    try {
                        zoom = Short.decode(child.getName());
                        scanXContainer(child);

                    } catch (NumberFormatException e) {
                        AppLog.w(this, e);
                    }

                }
            });
        }
    }

    protected abstract boolean doZoomContainer(Foc dir);


    private void scanXContainer(Foc dir) {

        if (doXContainer(dir)) {
            dir.foreachDir(new Foc.OnHaveFoc() {
                @Override
                public void run(Foc child) {
                    try {
                        x = Integer.decode(child.getName());
                        scanYContainer(child);

                    } catch (NumberFormatException e) {
                        AppLog.w(this, e);
                    }

                }
            });
        }
    }

    protected abstract boolean doXContainer(Foc dir);


    private void scanYContainer(Foc dir) {
        if (doYContainer(dir)) {
            dir.foreachFile(new Foc.OnHaveFoc() {
                @Override
                public void run(Foc child) {

                    try {
                        String[] parts = child.getName().split("\\.");

                        if (parts.length==2) {
                            y = Integer.decode(parts[0]);
                            ext = parts[1];

                            scanFile(child);
                        }


                    } catch (NumberFormatException e) {
                        AppLog.w(this, e);
                    }
                }
            });
        }
    }

    protected abstract boolean doYContainer(Foc dir);


    private void scanFile(Foc file) {
        if (file.isFile())
            doFile(file);
    }

    protected abstract void doFile(Foc file);


    public static boolean doDirectory(Foc file) {
        return file.isDir();
    }


    private static boolean isReal(File file) {
        try {
            final String c = file.getCanonicalPath();
            final String a = file.getAbsolutePath();

            return c.equals(a);

        } catch (IOException e) {
            return false;
        }
    }
}
