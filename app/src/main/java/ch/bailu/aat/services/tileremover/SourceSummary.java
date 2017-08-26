package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.util.MemSize;

public class SourceSummary implements SourceSummaryInterface {
    private final String name;
    public int count, countToRemove, countNew;
    public long size, sizeToRemove, sizeNew;


    public SourceSummary(String n) {
        name = n;
    }


    public void addFile(long length) {
        size += length;
        sizeNew += length;
        count++;
        countNew++;
    }

    public void addFileToRemove(long length) {
        sizeToRemove += length;
        countToRemove++;

        sizeNew -= length;
        countNew--;
    }


    public void addFileRemoved(long length) {
        size -= length;
        sizeToRemove -= length;

        count --;
        countToRemove--;
    }

    public void clear_rm() {
        sizeNew = size;
        countNew = count;

        sizeToRemove = 0;
        countToRemove = 0;
    }

    public void clear() {
        size = 0;
        count = 0;
        clear_rm();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public StringBuilder buildReport(StringBuilder builder) {
        builder.append(count);
        builder.append('-');
        builder.append(countToRemove);
        builder.append('=');
        builder.append(countNew);

        builder.append('\n');

        MemSize.describe(builder, (double)size);
        builder.append('-');
        MemSize.describe(builder, (double)sizeToRemove);
        builder.append('=');
        MemSize.describe(builder, (double)sizeNew);

        return builder;
    }

}
