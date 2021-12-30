## ARI Versions

Within the Asterisk project ARI is versioned, these version numbers are not the same as the Asterisk version and also unrelated to ARI4Java's version.
It is important to note the version of Asterisk you are running, so you can instantiate the library using the correct version.
While using `AriVersion.IM_FEELING_LUCKY` tries to determine the correct version, it is recommended to rather specify a version for application stability. Generally it is safe to use an older version of ARI on a new Asterisk, but not the other way around.

The table below is built up using the git tags to link the Asterisk version to the corresponding ARI version:

| Asterisk | ARI |
| :-- | :-- |
