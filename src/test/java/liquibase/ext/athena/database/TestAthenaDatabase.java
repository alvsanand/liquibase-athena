package liquibase.ext.athena.database;

import java.io.IOException;

import org.junit.Test;

import liquibase.database.DatabaseFactory;

public class TestAthenaDatabase {

    // @Test
    public void findDriver() throws IOException {
        DatabaseFactory.getInstance()
                .findDefaultDriver("jdbc:athena://AwsRegion={AWS_REGION};S3OutputLocation=s3://{AWS_S3_BUCKET};");
    }
}
