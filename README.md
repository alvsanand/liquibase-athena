# Liquibase Athena Extension

This is a Liquibase extension for connecting to an Amazon Athena database.

[Amazon Athena](https://docs.aws.amazon.com/athena/latest/ug/what-is.html) is an interactive query service that analyzes data directly in Amazon Simple Storage Service (Amazon S3) using standard SQL.

## Configuring the extension

These instructions will help you get the extension up and running on your local machine for development and testing purposes. This extension has a prerequisite of Liquibase core in order to use it. Liquibase core can be found at <https://www.liquibase.org/download>

### Pre-requisites

The following dependencies have to be available in classpath or installed manually using Maven:

* **[AthenaJDBC42](<https://docs.aws.amazon.com/athena/latest/ug/connect-with-jdbc.html>)**

### Liquibase CLI

Download [the latest released Liquibase extension](https://github.com/alvsanand/liquibase-athena/releases) `.jar` file and place it in the `liquibase/lib` install directory. If you want to use another location, specify the extension `.jar` and `AthenaJDBC42*.jar` file in the `classpath` of your [liquibase.properties file](https://docs.liquibase.com/workflows/liquibase-community/creating-config-properties.html).

### Maven

First, add the Maven repository to you ```pom.xml``` or ```settings.xml+```:

```xml  
<repository>
   <id>github</id>
   <url>https://maven.pkg.github.com/alvsanand/liquibase-athena</url>
   <snapshots>
      <enabled>true</enabled>
   </snapshots>
</repository>

  ```

Specify the Liquibase extension in the `<dependency>` section of ```pom.xml``` by adding the `org.liquibase.ext` dependency for the Liquibase plugin.

```xml  
<plugin>
   <groupId>org.liquibase</groupId>
   <artifactId>liquibase-maven-plugin</artifactId>
   <version>4.3.2</version>
   <configuration>
      <propertyFile>liquibase.properties</propertyFile>
   </configuration>
   <dependencies>
      <dependency>
         <groupId>org.liquibase.ext</groupId>
         <artifactId>liquibase-athena</artifactId>
         <version>${liquibase-athena.version}</version>
      </dependency>
   </dependencies>
</plugin>
  ```

### Extension Properties

Liquibase Athena extension needs to be configured to work so you must add the following properties:

```properties

# The S3 path where to store Liquibase tables.
liquibase.athena.liquibaseTableS3Location=s3://some_bucket/some_path
```
  
## Java call
  
```java
public class Application {
    public static void main(String[] args) {
        AthenaDatabase database = (AthenaDatabase) DatabaseFactory.getInstance().openDatabase(url, null, null, null, null);
        Liquibase liquibase = new Liquibase("liquibase/ext/changelog.generic.test.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }
}
```

## Issue Tracking

Any issues can be logged in the [Github issue tracker](https://github.com/alvsanand/liquibase-athena/issues).

## License

This project is licensed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).
