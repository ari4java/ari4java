# ARI4Java Changelog

## [Unreleased]
[Unreleased]: https://github.com/ari4java/ari4java/compare/v0.16.0...HEAD

## [0.16.0]
[0.16.0]: https://github.com/ari4java/ari4java/compare/v0.15.0...v0.16.0
### Added
- `AriVersion.ARI_4_1_4`
- `AriVersion.ARI_6_0_1`
- `AriVersion.ARI_7_0_1`

## [0.15.0]
[0.15.0]: https://github.com/ari4java/ari4java/compare/v0.14.0...v0.15.0
### Changed
- Library updates
- Refactoring test to use JUnit 5

### Fixed
- Map `local_ssrc` & `remote_ssrc` in `RTPstat` model to `Long` instead of `Integer` #180

## [0.14.0]
[0.14.0]: https://github.com/ari4java/ari4java/compare/v0.13.0...v0.14.0
### Fixed
- ARI API resource inconsistency due to using git history on `resource.json`, now git tags (aka released versions) are used

## [0.13.0]
[0.13.0]: https://github.com/ari4java/ari4java/compare/v0.12.2...v0.13.0
### Changed
- Library updates

### Added
- `AriVersion.ARI_8_0_0`

## [0.12.2]
[0.12.2]: https://github.com/ari4java/ari4java/compare/v0.12.1...v0.12.2
### Changed
- groupId changed from `ch.loway.oss.ari4java` to `io.github.ari4java`

### Added
## [0.12.1]
[0.12.1]: https://github.com/ari4java/ari4java/compare/v0.12.0...v0.12.1
### Added
- ARI 4.1.3, 5.1.1 & 7.0.0

## [0.12.0]
[0.12.0]: https://github.com/ari4java/ari4java/compare/v0.11.0...v0.12.0
### Fixes
- onFailure long after WS Connect error #156
- execute shutdown immediately #159

### Added
- WS reconnect count #158

## [0.11.0]
[0.11.0]: https://github.com/ari4java/ari4java/compare/v.0.10.0...v0.11.0
### Added
- Unit tests to increase coverage #11
- New ARI binding

## [0.10.0]
[0.10.0]: https://github.com/ari4java/ari4java/compare/v0.9.1...v.0.10.0
### Fixed
- UnsupportedOperationException #15
- Javadoc warnings #149
- If Url doesn't end with a slash add one #150
- Set `codegen` and `examples` to Java8 compatibility
### Added
- toString for generated model if there is an `id` or `name` field
- `AriWSHelper` class for convenient `onMessage` methods for each type #23
- Script to map Asterisk versions to ARI version

## [0.9.1] - 2020-02-23
[0.9.1]: https://github.com/ari4java/ari4java/compare/REL-0.9.0...v0.9.1
### Added
- Test connection when creating ARI #19
### Fixed
- Mistakenly removed the http aggregator

## [0.9.0] - 2020-02-20
[0.9.0]: https://github.com/ari4java/ari4java/compare/REL-0.8.1...REL-0.9.0
### Added
- SLF4J Logger
- Connection timeouts #85 #86
- Body JSON using Jackson #135
- WebSocket Connection Events #103
- Support SSL, 150MB limit for `ActionRecordings.getStoredFile()`

## [0.8.1] - 2020-01-25
[0.8.1]: https://github.com/ari4java/ari4java/compare/REL-0.8.0...REL-0.8.1
### Changed
- Exception messages #6
### Fixed
- Java 8 Compatibility #142

## [0.8.0] - 2019-12-23
[0.8.0]: https://github.com/ari4java/ari4java/compare/REL-070...REL-0.8.0
### :exclamation: **!! BREAKING CHANGES !!** 
- API Actions signatures only contain manditory params and returns a Request object which follows the builder/factory pattern

## [0.7.0] - 2019-12-22
[0.7.0]: https://github.com/ari4java/ari4java/compare/REL-061...REL-070
### Fixed
- Treat `fields` as `fields` not `variables` in body parameters
- fix `ActionRecordings.getStoredFile()` for `byte[]` & add `ARI.mailboxes()`

## [0.6.1] - 2019-11-07
[0.6.1]: https://github.com/ari4java/ari4java/compare/REL-060...REL-061
### Fixed
- Codegen bug fixes for object

## [0.6.0] - 2019-10-15
[0.6.0]: https://github.com/ari4java/ari4java/compare/REL-051...REL-060
### Change
- Project restructure
- Script to include all past and present versions of ARI

## [0.5.1] - 2019-10-15
[0.5.1]: https://github.com/ari4java/ari4java/compare/REL-050...REL-051
### Fixes
- Various fixes (Goodbye Naama!)

## [0.5.0] - 2019-01-07
[0.5.0]: https://github.com/ari4java/ari4java/compare/REL-045...REL-050
### Added
- Support java 9 (#108)
- code generation from gradle(#110)
- event interface inheritance(#106)
### Fixed
- unsubscribing from application correctly(#59)

## [0.4.5] - 2017-12-19
[0.4.5]: https://github.com/ari4java/ari4java/compare/REL-044...REL-045
### Added
- ARI 3.0.0 (#78)

## [0.4.4] - 2017-02-04
[0.4.4]: https://github.com/ari4java/ari4java/compare/REL-043...REL-044
### Added
- ARI 2.0.0 (#62) 
### Changed
- quicker deserialization (#63)

## [0.4.3] - 2016-11-30
[0.4.3]: https://github.com/ari4java/ari4java/compare/REL-042...REL-043
### Fixed
- Graham's AutoReconnect patch - #60

## [0.4.2] - 2016-10-21
[0.4.2]: https://github.com/ari4java/ari4java/compare/REL-041...REL-042
### Fixing 
 - URL Prefix regression #57 
 - Findbugs string concatenation #55

## [0.4.1] - 2016-10-17
[0.4.1]: https://github.com/ari4java/ari4java/compare/REL-040...REL-041
### Added
- Graham's AutoReconnect patch #52

## [0.4.0] - 2016-08-31
[0.4.0]: https://github.com/ari4java/ari4java/compare/REL-034...REL-040
### Added
- ARI 1.10.0 (Asterisk 14)
### Fixed
- some bugs

## [0.3.4] - 2016-01-30
[0.3.4]: https://github.com/ari4java/ari4java/compare/REL-033...REL-034
### Added
- ARI 1.9.0

## [0.3.3] - 2015-09-23
[0.3.3]: https://github.com/ari4java/ari4java/compare/REL-022...REL-032
### Added
- 201 statuses (bug #33)

## 0.3.2 - 2015-09-23
### Added
- ARI 1.8.0 (bug #32)

## 0.3.1 - 2015-03-20
### Fixed
- Disconnected ARI WS now throws an exception (bug #28)

## 0.3.0 - 2015-03-11
### Added
- ARI 1.7.0 (bug #28)

## [0.2.2] - 2014-11-09
[0.2.2]: https://github.com/ari4java/ari4java/compare/v011...REL-022
### Added
- ARI bindings for 1.5.0 as coming from the official Asterisk 13.0.0 release
- Added a minimal application under tests/ class ch.loway.oss.ari4java.sandbox.sample to be used as a style laboratory

## [0.1.1] - 2013-12-31
[0.1.1]: https://github.com/ari4java/ari4java/commits/v011
### Added
- Netty.io based HTTP and WebSocket implementation, factory, sync and async methods
- Imported the definitions for Asterisk 12.0.0 - ARI 1.0.0
- All objects are deserializable right out of JSON automatically
- Auto-generates all classes and compiles them
- Gradle build script
