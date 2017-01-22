package ch.bailu.aat.util.fs;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetAccess extends AbsAccess {

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
    public File toFile() {
        return new File(asset);
    }
}
