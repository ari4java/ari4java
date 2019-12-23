## ARI Versions

In the `codegen` folder there is a bash script: `getApis.sh` 
that will get and recurse the Asterisk source generating the data required for the APIs

Commit the changes as the Gradle build script always builds from those files

## Deployment

When a release is ready:


		export BINTRAY_USER=<user>
		export BINTRAY_KEY={bintray.txt}

		./gradlew clean test jar
		./gradlew bintrayUpload


