package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import ch.bailu.aat.util.ui.AppLog;

public abstract class TileScanner {

    private final File root;

    protected String source;
    protected short zoom;
    protected int x,y;
    protected String ext;

    public TileScanner(File r) {
        root = r;
    }

    public void scanZoomContainer() {
        source = root.getName();
        scanZoomContainer(root);
    }

    public void scanSourceContainer() {
        scanSourceContainer(root);
    }


    private void scanSourceContainer(File dir) {
        if (doDirectory(dir) && doSourceContainer(dir)) {

            dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File d, String name) {
                    source = name;
                    scanZoomContainer(new File(d, name));
                    return false;
                }
            });
        }
    }

    protected abstract boolean doSourceContainer(File dir);


    private void scanZoomContainer(File dir) {
        if (doDirectory(dir) && doZoomContainer(dir)) {
            dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File d, String name) {
                        try {
                            zoom = Short.decode(name);
                            scanXContainer(new File(d, name));

                        } catch (NumberFormatException e) {
                            AppLog.d(e, d.getName());
                        }

                    return false;
                }
            });
        }
    }

    protected abstract boolean doZoomContainer(File dir);


    private void scanXContainer(File dir) {
        if (doDirectory(dir) && doXContainer(dir)) {
            dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File d, String name) {
                    try {
                        x = Integer.decode(name);
                        scanYContainer(new File(d, name));

                    } catch (NumberFormatException e) {
                        AppLog.d(e, d.getName());
                    }
                    return false;
                }
            });
        }
    }

    protected abstract boolean doXContainer(File dir);


    private void scanYContainer(File dir) {
        if (doDirectory(dir) && doYContainer(dir)) {
            dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File d, String name) {
                        try {
                            String[] parts = name.split("\\.");

                            if (parts.length==2) {
                                y = Integer.decode(parts[0]);
                                ext = parts[1];

                                scanFile(new File(d, name));
                            }


                        } catch (NumberFormatException e) {
                            AppLog.d(e, d.getName());
                    }
                    return false;
                }
            });
        }
    }

    protected abstract boolean doYContainer(File dir);


    private void scanFile(File file) {
        if (file.isFile())
            doFile(file);
    }

    protected abstract void doFile(File file);


    public static boolean doDirectory(File file) {
        return file.isDirectory() && !file.isHidden() && isReal(file);
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
