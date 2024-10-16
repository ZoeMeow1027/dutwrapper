# DutWrapper

An unofficial wrapper for easier to use at [sv.dut.udn.vn - Da Nang University of Technology student page](http://sv.dut.udn.vn).

# Version
- Release version [![https://github.com/dutwrapper/dutwrapper-java](https://img.shields.io/github/v/release/dutwrapper/dutwrapper-java)](https://github.com/dutwrapper/dutwrapper-java/releases)
- Pre-release version [![https://github.com/dutwrapper/dutwrapper-java/tree/draft](https://img.shields.io/github/v/tag/dutwrapper/dutwrapper-java?label=pre-release%20tag)](https://github.com/dutwrapper/dutwrapper-java/tree/draft)
- [Summary change log](CHANGELOG.md) / [Entire source code changes](https://github.com/dutwrapper/dutwrapper-java/commits)
- Badge provided by [shields.io](https://shields.io/)

# Library requirements
- You will need JDK version 8 or above. Older version of JDK will result in compile error.
  - Currently I tested with Eclipse Temurin JDK with Hotspot v8 and v17.

# FAQ

## Branch in dutwrapper?
- `stable`/`main`: Default branch and main release.
- `draft`: Alpha branch. This branch is used for update my progress and it's very unstable. Use it at your own risk.

## I received error about login while running AccountTest
- If your received issue about `dut_account` environment variable not found, please manually add with format `username|password`.

## Wiki for this library?
- Unfortunately, I haven't done wiki yet.
- Instead you can navigate source code in `src/test/java/io/dutwrapper/dutwrapper/` to view them.

## I'm got issue or a feature request about this library. How should I do?
- Navigate to [issue tab](https://github.com/dutwrapper/dutwrapper-java/issues) on this repository to create a issue or feature request.

# Credit and license?
- License: [**MIT**](LICENSE)
- DISCLAIMER:
  - This project - dutwrapper - is not affiliated with [Da Nang University of Technology](http://sv.dut.udn.vn).
  - DUT, Da Nang University of Technology, web materials and web contents are trademarks and copyrights of [Da Nang University of Technology](http://sv.dut.udn.vn) school.
- Used third-party dependencies:
  - [Jsoup](https://github.com/jhy/jsoup): Licensed under the [MIT License](https://github.com/jhy/jsoup/blob/master/LICENSE).
  - [OkHttp3](https://github.com/square/okhttp): Licensed under the [Apache License 2.0](https://github.com/square/okhttp/blob/master/LICENSE.txt).
  - [Gson](https://github.com/google/gson): Licensed under the [Apache License 2.0](https://github.com/google/gson/blob/master/LICENSE).
