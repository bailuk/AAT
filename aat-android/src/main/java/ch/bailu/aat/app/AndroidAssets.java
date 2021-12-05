package ch.bailu.aat.app;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;

import ch.bailu.aat_lib.resources.AssetsInterface;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAsset;

public class AndroidAssets implements AssetsInterface {
    private final AssetManager assets;

    public AndroidAssets(Context context) {
        assets = context.getAssets();
    }

    @Override
    public Foc toFoc(String string) {
        return new FocAsset(assets, string);
    }

    @Override
    public String[] list(String assetPath) {
        try {
            return assets.list(assetPath);
        } catch (IOException e) {
            return new String[0];
        }
    }
}
