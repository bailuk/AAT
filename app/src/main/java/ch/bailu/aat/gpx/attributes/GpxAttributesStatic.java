package ch.bailu.aat.gpx.attributes;


import android.support.annotation.NonNull;

public class GpxAttributesStatic extends GpxAttributes {


    public static class Tag implements Comparable<Tag> {
        public Tag(Tag keyValue) {
            this(keyValue.key, keyValue.value);
        }

        public Tag(int k, String v) {
            key=k;
            value=v;
        }

        public final int key;
        public final String value;


        @Override
        public int compareTo(@NonNull Tag another) {
            return Integer.compare(key, another.key);
        }
    }

    private Tag[] tagList;


    public GpxAttributesStatic() {
        this(new Tag[]{});
    }

    public GpxAttributesStatic(Tag[] attr) {
        tagList=attr;
    }


    @Override
    public String get(int key) {
        int index = getIndex(key);

        if (index==size()) return NULL_VALUE;

        return tagList[index].value;
    }


    @Override
    public boolean hasKey(int key) {
        return getIndex(key) < size();
    }

    @Override
    public void put(int key, String value) {
        int index = getIndex(key);

        if (index == size()) {
            Tag[] newTagList = new Tag[size()+1];

            System.arraycopy(tagList, 0, newTagList, 0, tagList.length);
            tagList=newTagList;
        }

        tagList[index] = new Tag(key, value);
    }


    private int getIndex(int key) {
        for (int i=0; i<size(); i++) {
            if (tagList[i].key == key) return i;
        }
        return size();
    }


    @Override
    public int size() {
        return tagList.length;
    }

    @Override
    public String getAt(int i) {
        return tagList[i].value;
    }

    @Override
    public int getKeyAt(int i) {
        return tagList[i].key;
    }

/*
    @Override
    public void remove(String key) {
        int index = getIndex(key);

        if (index < size()) {
            Tag[] newTagList = new Tag[size()-1];

            int newIndex=0;
            for (int oldIndex=0; oldIndex<size(); oldIndex++) {
                if (oldIndex != index) {
                    newTagList[newIndex] = new Tag(tagList[oldIndex]);
                    newIndex++;
                }
            }
            tagList=newTagList;
        }
    }
*/
}
