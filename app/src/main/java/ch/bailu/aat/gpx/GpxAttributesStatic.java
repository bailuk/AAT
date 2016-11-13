package ch.bailu.aat.gpx;



public class GpxAttributesStatic extends GpxAttributes{
    public static final GpxAttributesStatic NULL_ATTRIBUTES =
            new GpxAttributesStatic();


    public static class Tag implements Comparable<Tag> {
        public Tag(Tag keyValue) {
            this(keyValue.key, keyValue.value);
        }

        public Tag(String k, String v) {
            key=k;
            value=v;
        }

        public final String key, value;


        @Override
        public int compareTo(Tag another) {
            return key.compareTo(another.key);
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
    public String get(String key) {
        int index = getIndex(key);

        if (index==size()) return null;

        return tagList[index].value;
    }

    @Override
    public String getValue(int index) {
        if (index < size()) {
            return tagList[index].value;
        }
        return null;
    }


    @Override
    public String getKey(int index) {
        if (index < size()) {
            return tagList[index].key;
        }
        return null;
    }


    @Override
    public void put(String key, String value) {
        int index = getIndex(key);

        if (index == size()) {
            Tag[] newTagList = new Tag[size()+1];

            System.arraycopy(tagList, 0, newTagList, 0, tagList.length);
            tagList=newTagList;
        }

        tagList[index] = new Tag(key, value);
    }


    private int getIndex(String key) {
        for (int i=0; i<size(); i++) {
            if (tagList[i].key.equals(key)) return i;
        }
        return size();
    }


    @Override
    public int size() {
        return tagList.length;
    }


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

}
