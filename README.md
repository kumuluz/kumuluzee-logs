# KumuluzEE Logs
[![Build Status](https://img.shields.io/travis/kumuluz/kumuluzee-logs/master.svg?style=flat)](https://travis-ci.org/kumuluz/kumuluzee-logs)

> Lightweight open-source microservice logging framework for the KumuluzEE framework

KumuluzEE Logs is a lightweight open-source logging framework specifically designed for logging microservices. 

It provides easy and efficient logging of common log events, such as logging method entries and exits, logging external resource invocations and other events. It also provides automated logging of parameters and performance metrics. 

KumuluzEE Logs has been designed to simplify logging for the developer. It introduces a @Log annotation, which can be used on a class or on a method to enable legging the method entry, parameters, method exit and optionally include performance metrics. 

In addition, KumuluzEE Logs also support logging with explicit commands. It provides logging methods for the developer, which can be used in the code directly. 

KumuluzEE Logs acts as a façade and provides a simple, common interface with the objective to abstract the underlying logging framework. This makes the logging process easier for the developer, standardizes how the logging is performed, and makes the code independent of the underlying logging framework. 

KumuluzEE Logs is designed to support different logging frameworks. Currently, KumuluzEE Logs provides support for Log4J2. In the future, other logging frameworks will be supported too (contributions are welcome).

To address the needs specific to logging microservices, KumuluzEE Logs can be easily configured to collect distributed logs into a centralized log management system, such as ELK (Elasticsearch, Logstash, Kibana) stack, Graylog, Splunk, etc. Furthermore, KumuluzEE Logs provides support for Apache Kafka and other approaches. 

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

**Resource invocation logging**

Additional functionality of `LogCommons` implementation is the ability to log and monitor invocations of external resources, for example databases and services. Resource monitoring allows you to log resource parameters and performance metrics. This functionality is available only through manual invocation of `LogCommons` methods.

Sample code of resource invocation monitoring:

```java
LogResourceContext context = logCommons.logResourceStart(
    LogLevel.TRACE,
    Marker.DATABASE,
    new LogResourceMessage().enableInvoke(invokeMessage).enableMetrics());

//...Read resource by id

logCommons.logResourceEnd(context);
```

In the sample above we invoke `logResourceStart` method, with parameters:
* LogLevel, which specifies different log levels (TRACE, ERROR, DEBUG...).
* Marker, which is an enum, implementing interface `com.kumuluz.ee.logs.markers.Marker`. You should implement Markers according to your needs.
* New `LogResourceMessage` instance, where we set the invocation message (see below) and enable metrics monitoring.

`logResourceStart` method returns `LogResourceContext` instance, which passes relavant information for logging end of resource invocation.

`InvokeMessage` variable is an instsance of `com.kumuluz.ee.logs.messages.ResourceInvokeLogMessage` interface and therefore must implement a method which returns a Map (`Map<String, String>`) of parameters. For example, the sample of InvocationMessage could be used to populate the Map the in the following way:

```java
InvocationMessage invokeMessage = new InvocationMessage("Invocation of database resource").
    addName("User").addParameter("id",id);
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

**Logging additional service information**

Additional information (service-name, version and environment) can be logged automatically when defined in KumuluzEE configuration (config.yaml):

```yaml
kumuluzee:
  service-name: customer-service
  version: 1.0.0
  env: dev
  ...
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
