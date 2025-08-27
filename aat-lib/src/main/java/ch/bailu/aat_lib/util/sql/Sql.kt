package ch.bailu.aat_lib.util.sql

object Sql {
    fun getCreateTableExpression(table: String, keyList: Array<String>, typeList: Array<String>): String {
        val expression = StringBuilder()
        expression
            .append("CREATE TABLE ")
            .append(table)
            .append(" (")

        for (i in keyList.indices) {
            if (i > 0) expression.append(", ")
            expression.append(keyList[i]).append(" ").append(typeList[i])
        }
        expression.append(")")
        return expression.toString()
    }

    fun getTableDropStatement(table: String): String {
        return "DROP TABLE IF EXISTS $table"
    }
}
