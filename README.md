## Spring Boot Starter for Presto
Spring Boot Starter for Presto

## edit pom.xml,add dependency

```javascript
<dependency>
    <groupId>com.funtime.bigdata.presto</groupId>
    <artifactId>presto-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## create application.properties

```javascript
============= PRESTO =========
presto.jdbc.driver=com.facebook.presto.jdbc.PrestoDriver
presto.jdbc.username=hive
presto.jdbc.password=
presto.jdbc.url=presto://192.168.100.225:8285/hive/default
============= HDFS ==========
hdfs.url=hdfs://hadoop.namenode:8020
```

## use in spring boot 

```javascript
@Autowired
@Qualifier("phoenixJdbcTemplate")
JdbcTemplate phoenixJdbcTemplate;
```

## use hive in spring boot 

```javascript
phoenixJdbcTemplate
```