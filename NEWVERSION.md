When adding a new version of the ARI:

- Copy the files from "Asterisk/rest-api/api-docs to the right foolder under "codegen_data"
- Add the ARI version in run.java under the code generator
- Run the code generator

In the main source tree:

- create the version to be used in AriVersion.java
- in ARI.java, edit the build() function to get you the correct objects


