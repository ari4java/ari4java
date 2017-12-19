These are MacOS specific directions, mostly because of file separators and only tried on MacOS
```bash
git clone https://github.com/l3nz/ari4java
cd <ari4java>/codegen
```
change line 17 of `ch/loway/oss/ari4java/codegen/run.java` to something better for your env than:

*public static String PROJECT = "/Users/lenz/varie/ari4java";*

Do something like this to keep the classpath length within reason, and mapping to wherever the jars are for these libs:
```bash
ln -s /Users/btofel/.m2/repository/com/fasterxml/jackson/core jackson_core_m2_cache_jars
javac -cp "jackson_core_m2_cache_jars/jackson-core/2.2.2/jackson-core-2.2.2.jar:jackson_core_m2_cache_jars/jackson-databind/2.2.2/jackson-databind-2.2.2.jar:jackson_core_m2_cache_jars/jackson-annotations/2.2.2/jackson-annotations-2.2.2.jar:." ch/loway/oss/ari4java/codegen/models/*.java
javac -cp "jackson_core_m2_cache_jars/jackson-core/2.2.2/jackson-core-2.2.2.jar:jackson_core_m2_cache_jars/jackson-databind/2.2.2/jackson-databind-2.2.2.jar:jackson_core_m2_cache_jars/jackson-annotations/2.2.2/jackson-annotations-2.2.2.jar:." ch/loway/oss/ari4java/codegen/run.java
```
Hopefully the above built without errors, so run the codegen part that builds stuff against the Asterisk Swagger-based API:
```bash
java -cp "jackson_core_m2_cache_jars/jackson-core/2.2.2/jackson-core-2.2.2.jar:jackson_core_m2_cache_jars/jackson-databind/2.2.2/jackson-databind-2.2.2.jar:jackson_core_m2_cache_jars/jackson-annotations/2.2.2/jackson-annotations-2.2.2.jar:." ch.loway.oss.ari4java.codegen.run
```
And hopefully that spews a bunch of json files into your codegen-data folder, you're ready to build with Gradle.
Java 8 is the best bet, there are naming clashes with Java 9 at the very least.
