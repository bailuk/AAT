package ch.bailu.aat.preferences.presets;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.general.SolidPresetCount;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class SolidPreset extends SolidIndexList {

    final public static String KEY="preset";


    public SolidPreset(Context c) {
        super(c, KEY);
    }


    public static int getPresetFromFile(Foc file) {
        int preset = 0;
        file = file.parent();

        if (file != null && file.isDir()) {
            String name = file.getName();

            if (name != null) {
                name = name.replace(AppDirectory.PRESET_PREFIX, "");

                try {
                    preset = Integer.parseInt(name);
                    preset = Math.max(preset, 0);
                    preset = Math.min(preset, SolidPresetCount.MAX);
                } catch (Exception e) {
                    preset = 0;
                }
            }
        }

        return preset;
    }


    @Override
    public int length() {
        return new SolidPresetCount(getContext()).getValue();
    }

    @Override
    public String getValueAsString(int i) {
        return smet(i).getValueAsString();
    }

    @Override
    public String getValueAsString() {
        return smet().getValueAsString();
    }


    public SolidMET smet() {
        return smet(getIndex());
    }

    private SolidMET smet(int i) {
        return new SolidMET(getContext(), i);
    }


    @Override
    public boolean hasKey(String key) {
        return super.hasKey(key) || smet().hasKey(key);
    }



    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_preset);
    }


    public Foc getDirectory() {
        return AppDirectory.getTrackListDirectory(getContext(),getIndex());
    }


    public String getDirectoryName() {
        return getDirectory().getPathName();
    }



}
