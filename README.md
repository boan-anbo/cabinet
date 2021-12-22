# Usage:

## Tags:

```
[[KEY|VALUE|NOTE]]
```

Example:
1. [[ARGUMENT|Key|Note]]
# Swagger Url

http://localhost:18081/swagger-ui.html

# Key to compiling in Maven and Spring.

Because this is a mixed project with Kotlin and Java. I need to first let Kotlin compiles the KT files, and then let Java tooling including both Maven and Spring to compile to Java.

To do this, there are several requirements:

1. KT has to be in a separate package from the Java files. Perhaps mixing is possible but I haven't tried it.

2. In Maven pom, put Kotlin's compile plugin BEFORE others so that it runs first, especially before the Maven plugin for packaging.

3. In the Maven compile plugin, add two steps to make compiling a four-step process: 
    1. Kotlin compile
    2. Kotlin test compile.
    3. Java compile
    4. Java test compile.

```xml

<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-compiler-plugin</artifactId>
<executions>
   <!-- Replacing default-compile as it is treated specially by maven -->
   <execution>
      <id>default-compile</id>
      <phase>none</phase>
   </execution>
   <!-- Replacing default-testCompile as it is treated specially by maven -->
   <execution>
      <id>default-testCompile</id>
      <phase>none</phase>
   </execution>
   <execution>
      <id>java-compile</id>
      <phase>compile</phase>
      <goals> <goal>compile</goal> </goals>
   </execution>
   <execution>
      <id>java-test-compile</id>
      <phase>test-compile</phase>
      <goals> <goal>testCompile</goal> </goals>
   </execution>
</executions>
<configuration>
   <source>16</source>
   <target>16</target>
</configuration>
```

4. To compile for spring run:
`mvn clean package spring-boot:repackage`

5. Keep in mind, Intellij compiling/packaging, Maven compiling/packaging, Spring compiling/packaging are all different processes.

6. To get QueryDsl Entity, right click Maven cabinet, and `Generate Sources and Update Folder`
