package ch.bailu.aat_lib.util.sql;

public class DbException extends RuntimeException {

    public DbException(String msg) {
        super(msg);
    }

    public DbException(Exception e) {
        super(e);
    }
}
