package ch.bailu.aat.gpx;



public class GpxAttributes {
    public static final GpxAttributes NULL_ATTRIBUTES = 
            new GpxAttributes();


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


    public GpxAttributes() {
        this(new Tag[]{});
    }

    public GpxAttributes(Tag[] attr) {
        tagList=attr;
    }


    public String get(String key) {
        int index = getIndex(key);

        if (index==size()) return null;

        return tagList[index].value;
    }


    public String getValue(int index) {
        if (index < size()) {
            return tagList[index].value;
        }
        return null;
    }


    public String getKey(int index) {
        if (index < size()) {
            return tagList[index].key;
        }
        return null;
    }



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
        key.toLowerCase();

        for (int i=0; i<size(); i++) {
            if (tagList[i].key.equals(key)) return i;
        }
        return size();
    }

/*
    public boolean isEmpty() {
        return size()==0;
    }
*/
    public int size() {
        return tagList.length;
    }



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




    public StringBuilder toHtml(final StringBuilder builder) {
        for (int i=0; i<size(); i++) {
            if (getKey(i).equals("name") || getKey(i).equals("display_name")) {
                appendKeyValueBold(builder,getKey(i), getValue(i));
            } else {
                appendKeyValue(builder, getKey(i), getValue(i));                
            }
            builder.append("<br>");
        }

        builder.append("<hr>");

        return builder;
    }

    
    public static void appendKeyValueBold(StringBuilder builder, String key, String value) {
        builder.append("<b>");
        appendKeyValue(builder,key,value);
        builder.append("</b>");
    }
    
    public static void appendKeyValue(StringBuilder builder, String key, String value) {
        builder.append(key);
        builder.append("=");
        builder.append(value);
    }

}
