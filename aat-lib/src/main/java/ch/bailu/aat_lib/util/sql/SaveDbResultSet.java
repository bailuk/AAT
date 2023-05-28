package ch.bailu.aat_lib.util.sql;

import ch.bailu.aat_lib.logger.AppLog;

public class SaveDbResultSet implements DbResultSet {
    private final DbResultSet resultSet;

    public SaveDbResultSet(DbResultSet resultSet) {
        this.resultSet = resultSet;

    }
    @Override
    public boolean moveToFirst() {
        try {
            return resultSet.moveToFirst();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return false;
    }

    @Override
    public boolean moveToNext() {
        try {
            return resultSet.moveToNext();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return false;
    }

    @Override
    public boolean moveToPrevious() {
        try {
            return resultSet.moveToPrevious();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return false;
    }

    @Override
    public boolean moveToPosition(int pos) {
        try {
            return resultSet.moveToPosition(pos);
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return false;
    }

    @Override
    public int getPosition() {
        try {
            return resultSet.getPosition();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return 0;
    }

    @Override
    public int getCount() {
        try {
            return resultSet.getCount();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return 0;
    }

    @Override
    public String getString(String column) {
        try {
            return resultSet.getString(column);
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return "";
    }

    @Override
    public long getLong(String column) {
        try {
            return resultSet.getLong(column);
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return 0L;
    }

    @Override
    public float getFloat(String column) {
        try {
            return resultSet.getFloat(column);
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return 0f;
    }

    @Override
    public boolean isClosed() {
        try {
            return resultSet.isClosed();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
        return true;
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (Exception e) {
            AppLog.e(this, e);
        }
    }
}
