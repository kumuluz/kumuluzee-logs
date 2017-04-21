# KumuluzEE Logs
[![Build Status](https://img.shields.io/travis/kumuluz/kumuluzee-logs/master.svg?style=flat)](https://travis-ci.org/kumuluz/kumuluzee-logs)

> Lightweight open-source logging framework for microservice framework KumuluzEE

KumuluzEE Logs is an open-source logging framework with the functionality for logging in microservices. 
The library enables common logging functionalities, such as logging entries and exits from methods as well as logging external resources invocation. When this method are used, invocation parameters and performance monitoring can be automatically performed. Furthermore, custom logging methods for developers are available as well.
KumuluzEE Logs methods are defined with interfaces and can support multiple implementation of logging frameworks. Currently, Log4J2 implementation for KumuluzEE Logs is available allowing customization of logging collection and storage, for example, centralized logging with ELK stack, storing logs in database, sending logs through Apache Kafka, etc.

## Usage
KumuluzEE defines interfaces for common logging features. Therefore, to use the logging you need to include a dependency to implementation library. Currently, Log4j2 is supported and you add the dependency:

```xml
<dependency>
   <artifactId>kumuluzee-logs-log4j2</artifactId>
   <groupId>com.kumuluz.ee.logs</groupId>
   <version>${kumuluzee-logs.version}</version>
</dependency>
```

**Developer logging**

To use the developer logging functionality get a new `Logger` instance by using `LogManager`:

```java
private static final Logger LOG = LogManager.getLogger(CustomerResourceSample.class.getName());
```

Logger defines multiple overloaded methods for each logging level (error, warning, info, debug, trace), for example:

```java
LOG.trace("Trace log with String only");
LOG.info("Info log with parameter: {}", someVariable);
LOG.error("Error with exception log", exception);
LOG.error(exception); //exception only
```

**Common Logging**

Additional common logging is available through `LogCommons` interface, method entry and method exit logging can be used by using `@Log` annotation used at class level or method level or by invoking the methods manually. Resource logging can be used only by manual method invocation.

Example for using `@Log` at Class level:

```java 
@Log
public class SomeClass {
    //Implementation
}
```

You can define additional attributes in `@Log` annotation for monitoring method execution duration and disabling method invocation details. Observe three different possibilities for configuring method entry and exit logging:


```java
@Log
@Log(LogParams.METRICS)
@Log(value = LogParams.METRICS, methodCall = false)
```

**Add log4j2 logging configuration**

The configuration for Log4j2 library must be available for the application to load it. Please refer to Log4j2 documentation for rules regarding how the configuration is loaded. Sample configuration, which should be in a file named `log4j2.xml` and located in `src/main/resources`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration name="config-name">
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %p %marker %m %X %ex %n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
```

**Build the microservice**

Ensure you have JDK 8 (or newer), Maven 3.2.1 (or newer) and Git installed.
    
Build the logs library with command:

```bash
    mvn install
```
    
Build archives are located in the modules respected folder `target` and local repository `.m2`.

**Run the microservice**

Use the following command to run the sample from Windows CMD:
```
java -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -cp target/classes;target/dependency/* com.kumuluz.ee.EeApplication 
```

## Changelog

Recent changes can be viewed on Github on the [Releases Page](https://github.com/TFaga/KumuluzEE/releases)

## Contribute

See the [contributing docs](https://github.com/kumuluz/kumuluzee-logs/blob/master/CONTRIBUTING.md)

When submitting an issue, please follow the [guidelines](https://github.com/kumuluz/kumuluzee-logs/blob/master/CONTRIBUTING.md#bugs).

When submitting a bugfix, write a test that exposes the bug and fails before applying your fix. Submit the test alongside the fix.

When submitting a new feature, add tests that cover the feature.

## License

MIT
