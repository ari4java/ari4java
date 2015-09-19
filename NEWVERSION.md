When adding a new version of the ARI:

- Copy the files from "Asterisk/rest-api/api-docs" to the right folder under "codegen_data"
- Add the ARI version in run.java under the code generator
- Create folders like "generated.ARI_1_2_3", "generated.ARI_1_2_3.actions" and "generated.ARI_1_2_3.models"
  or the code generator will fail
- Run the code generator

In the main source tree:

- create the version to be used in AriVersion.java



(Obsolete)

- in ARI.java, edit the build() function to get you the correct objects


