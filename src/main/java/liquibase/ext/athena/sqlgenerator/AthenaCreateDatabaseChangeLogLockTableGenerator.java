package liquibase.ext.athena.sqlgenerator;

import liquibase.database.Database;
import liquibase.ext.athena.database.AthenaDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.sqlgenerator.core.CreateDatabaseChangeLogLockTableGenerator;
import liquibase.statement.core.CreateDatabaseChangeLogLockTableStatement;
import liquibase.statement.core.RawSqlStatement;

public class AthenaCreateDatabaseChangeLogLockTableGenerator extends CreateDatabaseChangeLogLockTableGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(CreateDatabaseChangeLogLockTableStatement statement, Database database) {
        return super.supports(statement, database) && database instanceof AthenaDatabase;
    }

    @Override
    public Sql[] generateSql(CreateDatabaseChangeLogLockTableStatement statement, Database database,
            SqlGeneratorChain sqlGeneratorChain) {
        AthenaDatabase athenaDatabase = (AthenaDatabase) database;

        RawSqlStatement createTableStatement = new RawSqlStatement(
                athenaDatabase.getCreateTableStatement(database.getDatabaseChangeLogLockTableName(),
                        "id INT, locked BOOLEAN, lockgranted timestamp, lockedby STRING"));

        return SqlGeneratorFactory.getInstance().generateSql(createTableStatement, database);

    }

}
