package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidTrimSize;

public class MapSummary implements MapSummaryInterface {
    private String name = "";
    private int hash;
    public long size, count, size_rm, countToRemove, newSize, count_new;



    public void clear_all() {
        setName("");
        clear();
        clear_rm();
    }

    public void log() {
        AppLog.d(this, name + " "+ count + " "+ size / (1024*1024) + "MB");
    }


    public void addFile(long length) {
        size += length;
        newSize += length;
        count++;
        count_new++;
    }

    public void addFileToRemove(long length) {
        size_rm += length;
        countToRemove++;

        newSize -= length;
        count_new --;
    }


    public void addFileRemoved(long length) {
        size -= length;
        size_rm -= length;

        count --;
        countToRemove--;
    }

    public void clear_rm() {
        newSize = size;
        count_new = count;

        size_rm = 0;
        countToRemove = 0;
    }

    public void clear() {
        size = 0;
        count = 0;
        clear_rm();
    }


    public synchronized void setName(String n) {
        name = n;
        hash = name.hashCode();
    }


    @Override
    public int hashCode() {
        return hash;
    }



    @Override
    public synchronized boolean isValid() {
        return name.length()>0;
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
        builder.append(count_new);

        builder.append('\n');

        SolidTrimSize.buildSizeText(builder, size);
        builder.append('-');
        SolidTrimSize.buildSizeText(builder, size_rm);
        builder.append('=');
        SolidTrimSize.buildSizeText(builder, newSize);

        return builder;
    }

}
