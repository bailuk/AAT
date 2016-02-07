package ch.bailu.aat.services.cache;

import android.util.SparseArray;
import ch.bailu.aat.helpers.CleanUp;

public class FreeLater implements CleanUp {
    private final static int INITIAL_CAPACITY=20;
    private final SparseArray<ObjectHandle> table = new SparseArray<ObjectHandle>(INITIAL_CAPACITY);

    
    public void freeLater(ObjectHandle handle) {
        int key=handle.toString().hashCode();
        
        if (table.get(key) == null) {
            table.put(key, handle);
        } else {
            handle.free();
        }
    }

    
    public void freeAll() {
        for (int i = 0; i<table.size(); i++) {
            table.valueAt(i).free();
        }
        table.clear();
    }

    
    @Override
    public void cleanUp() {
        freeAll();
    }
}
