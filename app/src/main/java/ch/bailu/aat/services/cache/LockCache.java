package ch.bailu.aat.services.cache;

import java.io.Closeable;

public class LockCache<E extends ObjectHandle>  implements Closeable {
    private E[]    objects;
    private long[] access;
    private int    size;



    public LockCache(int capacity) {
        objects = (E[]) new ObjectHandle[capacity];
        access = new long[capacity];
        size = 0;
    }


    public int size() {
        return size;
    }


    public E get(int i) {
        return objects[i];
    }

    public E use(int i) {
        objects[i].access();
        access[i] = System.currentTimeMillis();
        return objects[i];
    }


    public void add(E handle) {
        int i;

        if (size < objects.length) {
            i = size;
            size++;

        } else {
            i = indexOfOldest();
            objects[i].free();
        }

        objects[i] = handle;

        use(i);
    }


    private int indexOfOldest() {
        int x=0;

        for (int i = 1; i < size; i++) {
            if (access[i] < access[x]) {
                x=i;
            }
        }
        return x;
    }


    @Override
    public void close() {
        reset();
    }


    public void reset() {
        for (int i=0; i<size; i++) {
            objects[i].free();
        }
        size=0;
    }


    public void ensureCapacity(int capacity) {
        if (capacity > objects.length) {
            resizeCache(capacity);
        }
    }


    private void resizeCache(int capacity) {
        final E[] newObjects= (E[]) new ObjectHandle[capacity];
        final long newAccess[] = new long[capacity];

        final int l = Math.min(newObjects.length, objects.length);
        int x,i;

        for (i=0; i<l; i++) {
            newObjects[i]= objects[i];
            newAccess[i]=access[i];
        }

        for (x=i; x<size; x++) {
            objects[x].free();
        }

        objects = newObjects;
        access = newAccess;

        size = Math.min(size, objects.length);
    }
}
