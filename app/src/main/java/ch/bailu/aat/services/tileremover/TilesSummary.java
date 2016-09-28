package ch.bailu.aat.services.tileremover;

import java.io.File;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidTrimSize;

public class TilesSummary implements TilesSummaryInterface{
    private String name = "";
    private int hash;
    public long size, count, size_rm, count_rm, size_new, count_new;



    public void clear_all() {
        setName("");
        clear();
        clear_rm();
    }

    public void log() {
        AppLog.d(this, name + " "+ count + " "+ size / (1024*1024) + "MB");
    }


    public void inc(long length) {
        size += length;
        size_new += length;
        count++;
        count_new++;
    }

    public void inc_rm(long length) {
        size_rm += length;
        count_rm++;

        size_new -= length;
        count_new --;
    }


    public void inc_removed(long length) {
        size -= length;
        size_rm -= length;

        count --;
        count_rm --;
    }

    public void clear_rm() {
        size_new = size;
        count_new = count;

        size_rm = 0;
        count_rm = 0;
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
    public long getSize() {
        return size;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public StringBuilder buildReport(StringBuilder builder) {
        builder.append(count);
        builder.append('-');
        builder.append(count_rm);
        builder.append('=');
        builder.append(count_new);

        builder.append('\n');

        SolidTrimSize.buildSizeText(builder, size);
        builder.append('-');
        SolidTrimSize.buildSizeText(builder, size_rm);
        builder.append('=');
        SolidTrimSize.buildSizeText(builder, size_new);

        return builder;
    }

}
