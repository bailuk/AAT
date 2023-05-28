package ch.bailu.aat_lib.preferences.presets;

import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPresetCount;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class SolidPreset extends SolidIndexList {

    final public static String KEY="preset";

    public SolidPreset(StorageInterface s) {
        super(s, KEY);
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
        return new SolidPresetCount(getStorage()).getValue();
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
        return new SolidMET(getStorage(), i);
    }


    @Override
    public boolean hasKey(String key) {
        return super.hasKey(key) || smet().hasKey(key);
    }

    @Override
    public String getLabel() {
        return Res.str().p_preset();
    }

    public Foc getDirectory(SolidDataDirectory sdirectory) {
        return AppDirectory.getTrackListDirectory(sdirectory,getIndex());
    }
}
