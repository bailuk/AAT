package ch.bailu.aat.services.cache;

import java.io.Closeable;

public class LockCache<E extends ObjectHandle>  implements Closeable {
    private E[] array;
    private int size;


    public LockCache(int capacity) {
        array = (E[]) new ObjectHandle[capacity];
        size = 0;
    }


    public int size() {
        return size;
    }


    public E get(int i) {
        return array[i];
    }

    public E use(int i) {
        array[i].access();
        return array[i];
    }


    public void add(E handle) {
        int i;

        if (size < array.length) {
            i = size;
            size++;

        } else {
            i = indexOfOldest();
            array[i].free();
        }

        array[i] = handle;

    }


    private int indexOfOldest() {
        int x=0;
        for (int i = 1; i < size; i++) {
            if (array[i].getAccessTime() < array[x].getAccessTime()) {
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
            array[i].free();
        }
        size=0;
    }


    public void ensureCapacity(int capacity) {
        if (capacity > array.length) {
            resizeCache(capacity);
        }

    }

    private void resizeCache(int capacity) {
        final E[] newArray= (E[]) new ObjectHandle[capacity];
        final int l = Math.min(newArray.length, array.length);
        int x,i;

        for (i=0; i<l; i++) {
            newArray[i]=array[i];
        }

        for (x=i; x<size; x++) {
            array[x].free();
        }

        array = newArray;
        size = Math.min(size, array.length);
    }
}
