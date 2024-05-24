package liquibase.ext.athena.database;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.exception.DatabaseException;
import liquibase.ext.athena.configuration.AthenaConfiguration;
import liquibase.structure.DatabaseObject;

public class AthenaDatabase extends AbstractJdbcDatabase {
    private static final int HIGH_PRIORITY = 10;

    private Set<String> athenaReservedWords = new HashSet<String>();

    private static final String NAME = "athena";
    private static final String PRODUCT_NAME = "AWS.Athena";

    private static final String JDBC_PREFIX = "jdbc:athena:";
    private static final String DEFAULT_JDBC_DRIVER = "com.amazon.athena.jdbc.AthenaDriver";
    private static final int DEFAULT_JDBC_PORT = 443;

    public AthenaDatabase() {
        super.setCurrentDateTimeFunction("CURRENT_TIMESTAMP");

        athenaReservedWords.addAll(Arrays.asList("ALL",
                "ALTER",
                "AND",
                "ARRAY",
                "AS",
                "AUTHORIZATION",
                "BETWEEN",
                "BIGINT",
                "BINARY",
                "BOOLEAN",
                "BOTH",
                "BY",
                "CASE",
                "CASHE",
                "CAST",
                "CHAR",
                "COLUMN",
                "COMMIT",
                "CONF",
                "CONSTRAINT",
                "CREATE",
                "CROSS",
                "CUBE",
                "CURRENT_DATE",
                "CURRENT_PATH",
                "CURRENT_TIME",
                "CURRENT_TIMESTAMP",
                "CURRENT_USER",
                "CURRENT",
                "CURSOR",
                "DATABASE",
                "DATE",
                "DAYOFWEEK",
                "DEALLOCATE",
                "DECIMAL",
                "DELETE",
                "DESCRIBE",
                "DISTINCT",
                "DOUBLE",
                "DROP",
                "ELSE",
                "END",
                "ESCAPE",
                "EXCEPT",
                "EXCHANGE",
                "EXECUTE",
                "EXISTS",
                "EXTENDED",
                "EXTERNAL",
                "EXTRACT",
                "FALSE",
                "FETCH",
                "FIRST",
                "FLOAT",
                "FLOOR",
                "FOLLOWING",
                "FOR",
                "FOREIGN",
                "FROM",
                "FULL",
                "FUNCTION",
                "GRANT",
                "GROUP",
                "GROUPING",
                "HAVING",
                "IF",
                "IMPORT",
                "IN",
                "INNER",
                "INSERT",
                "INT",
                "INTEGER",
                "INTERSECT",
                "INTERVAL",
                "INTO",
                "IS",
                "JOIN",
                "LAST",
                "LATERAL",
                "LEFT",
                "LESS",
                "LIKE",
                "LOCAL",
                "LOCALTIME",
                "LOCALTIMESTAMP",
                "MACRO",
                "MAP",
                "MORE",
                "NATURAL",
                "NONE",
                "NORMALIZE",
                "NOT",
                "NULL",
                "NUMERIC",
                "OF",
                "ON",
                "ONLY",
                "OR",
                "ORDER",
                "OUT",
                "OUTER",
                "OVER",
                "PARTIALSCAN",
                "PARTITION",
                "PERCENT",
                "PRECEDING",
                "PRECISION",
                "PREPARE",
                "PRESERVE",
                "PRIMARY",
                "PROCEDURE",
                "RANGE",
                "READS",
                "RECURSIVE",
                "REDUCE",
                "REFERENCES",
                "REGEXP",
                "REVOKE",
                "RIGHT",
                "RLIKE",
                "ROLLBACK",
                "ROLLUP",
                "ROW",
                "ROWS",
                "SELECT",
                "SET",
                "SKIP",
                "SMALLINT",
                "START,TABLE",
                "TABLE",
                "TABLESAMPLE",
                "THEN",
                "TIME",
                "TIMESTAMP",
                "TO",
                "TRANSFORM",
                "TRIGGER",
                "TRUE",
                "TRUNCATE",
                "UNBOUNDED,UNION",
                "UNESCAPE",
                "UNION",
                "UNIQUEJOIN",
                "UNNEST",
                "UPDATE",
                "USER",
                "USING",
                "UTC_TIMESTAMP",
                "VALUES",
                "VARCHAR",
                "VIEWS",
                "WHEN",
                "WHERE",
                "WINDOW",
                "WITH"));
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return conn.getURL().startsWith(JDBC_PREFIX);
    }

    @Override
    public String getDefaultDriver(String url) {
        if (String.valueOf(url).startsWith(JDBC_PREFIX)) {
            return DEFAULT_JDBC_DRIVER;
        }
        return null;
    }

    @Override
    public Integer getDefaultPort() {
        return DEFAULT_JDBC_PORT;
    }

    @Override
    public String getDefaultSchemaName() {
        return null;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return PRODUCT_NAME;
    }

    @Override
    public int getPriority() {
        return HIGH_PRIORITY;
    }

    @Override
    public boolean isReservedWord(String tableName) {
        if (super.isReservedWord(tableName)) {
            return true;
        }

        return athenaReservedWords.contains(tableName.toUpperCase());

    }

    @Override
    public String escapeObjectName(String objectName, final Class<? extends DatabaseObject> objectType) {
        if (objectName != null) {
            // Athena recommend object names in lowercase. Athena will lowercase the name
            // for you to run the query.
            objectName = objectName.trim().toLowerCase();
            if (mustQuoteObjectName(objectName, objectType)) {
                return quoteObject(objectName, objectType);
            } else if (quotingStrategy == ObjectQuotingStrategy.QUOTE_ALL_OBJECTS) {
                return quoteObject(objectName, objectType);
            }
        }
        return objectName;
    }

    @Override
    public boolean requiresPassword() {
        return false;
    }

    @Override
    public boolean requiresUsername() {
        return true;
    }

    @Override
    public boolean isCaseSensitive() {
        return false;
    }

    @Override
    public boolean isAutoCommit() throws DatabaseException {
        return true;
    }

    @Override
    public void setAutoCommit(boolean b) {
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public boolean supportsTablespaces() {
        return false;
    }

    @Override
    public boolean supportsSequences() {
        return false;
    }

    @Override
    public boolean supportsSchemas() {
        return true;
    }

    @Override
    public boolean supportsAutoIncrement() {
        return false;
    }

    @Override
    public boolean supportsRestrictForeignKeys() {
        return false;
    }

    @Override
    public boolean supportsDropTableCascadeConstraints() {
        return false;
    }

    @Override
    public boolean supportsDDLInTransaction() {
        return false;
    }

    @Override
    public boolean supportsPrimaryKeyNames() {
        return false;
    }

    /*********************************
     * Custom code
     *********************************/

    public String getLiquibaseTableS3Location() {
        return AthenaConfiguration.LIQUIBASE_TABLE_S3_LOCATION.getCurrentValue();
    }

    public String getCreateTableStatement(String tableName, String columns) {
        String fullTableName = escapeTableName(getLiquibaseCatalogName(), getLiquibaseSchemaName(), tableName);

        String location = String.format("%s/%s", StringUtils.removeEnd(getLiquibaseTableS3Location(), "/"),
                tableName);

        return String.format(
                "CREATE TABLE IF NOT EXISTS %s (%s) LOCATION '%s' TBLPROPERTIES ( 'table_type' = 'ICEBERG')",
                fullTableName, columns, location);
    }
}
