package ch.bailu.aat_lib.util.sql;

public class Sql {
    public static String getCreateTableExpression(String table, String[] keyList, String[] typeList) {
        StringBuilder expression = new StringBuilder();
        expression
                .append("CREATE TABLE ")
                .append(table)
                .append(" (");

        for (int i = 0; i < keyList.length; i++) {
            if (i > 0) expression.append(", ");
            expression.append(keyList[i]).append(" ").append(typeList[i]);
        }
        expression.append(")");
        return expression.toString();
    }

    public static String getTableDropStatement(String table) {
        return "DROP TABLE IF EXISTS " + table;
    }
}
