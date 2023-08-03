package liquibase.ext.athena.sqlgenerator;

import liquibase.database.Database;
import liquibase.ext.athena.database.AthenaDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.sqlgenerator.core.CreateDatabaseChangeLogTableGenerator;
import liquibase.statement.core.CreateDatabaseChangeLogTableStatement;
import liquibase.statement.core.RawSqlStatement;

public class AthenaCreateDatabaseChangeLogTableGenerator extends CreateDatabaseChangeLogTableGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(CreateDatabaseChangeLogTableStatement statement, Database database) {
        return super.supports(statement, database) && database instanceof AthenaDatabase;
    }

    @Override
    protected String getCharTypeName(Database database) {
        return "TEXT";
    }

    @Override
    protected String getDateTimeTypeString(Database database) {
        return "TIMESTAMP";
    }

    @Override
    public Sql[] generateSql(CreateDatabaseChangeLogTableStatement statement, Database database,
            SqlGeneratorChain sqlGeneratorChain) {
        AthenaDatabase athenaDatabase = (AthenaDatabase) database;

        RawSqlStatement createTableStatement = new RawSqlStatement(athenaDatabase.getCreateTableStatement(
                database.getDatabaseChangeLogTableName(),
                "ID STRING, AUTHOR STRING, FILENAME STRING, DATEEXECUTED timestamp, ORDEREXECUTED INT," +
                        "EXECTYPE STRING, MD5SUM STRING, DESCRIPTION STRING, COMMENTS STRING, TAG STRING," +
                        "LIQUIBASE STRING, CONTEXTS STRING, LABELS STRING, DEPLOYMENT_ID STRING"));

        return SqlGeneratorFactory.getInstance().generateSql(createTableStatement, database);
    }

}
