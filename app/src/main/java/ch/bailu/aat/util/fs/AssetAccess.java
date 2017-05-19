package ch.bailu.aat.util.fs;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ch.bailu.simpleio.io.Access;

public class AssetAccess extends Access {

    private final String asset;
    private final AssetManager manager;


    public AssetAccess(AssetManager m, String a) {
        manager = m;
        asset = a;
    }

    @Override
    public InputStream open_r() throws IOException {
        return manager.open(asset);
    }

    @Override
    public OutputStream open_w() throws IOException {
        throw new IOException();
    }

    @Override
    public long lastModified() {
        return System.currentTimeMillis();
    }


    public static ArrayList<String> listAssets(AssetManager am, String path) {
        ArrayList<String> r = new ArrayList();

        try {
            String [] files = am.list(path);
            for (String f: files) {
                r.add(path+ "/" + f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return r;
    }

}
