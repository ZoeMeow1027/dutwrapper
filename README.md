# DutWrapper

An unofficial wrapper for easier to use at [sv.dut.udn.vn - Da Nang University of Technology student page](http://sv.dut.udn.vn).

# Building requirements
- JDK (tested with Eclipse Temurin JDK with Hotspot v17.0.2 and v8).

# FAQ

## Where can I found library changelog?
If you want to:
- View major changes: [Click here](CHANGELOG.md).
- View entire source code changes: [Click here](https://github.com/dutwrapper/dutwrapper-java/commits).
  - You will need to change branch if you want to view changelog for stable/draft version.

## Branch in dutwrapper?
- `stable`/`main`: Default branch and main release.
- `draft`: Alpha branch. This branch is used for update my progress and it's very unstable. Use it at your own risk.

## I received error about login while running AccountTest?
- Did you mean this error: `dut_account environment variable not found. Please, add or modify this environment in format "username|password"`?
- If so, you will need to add environment variable named `dut_account` with syntax `studentid|password`.

## Wiki, or manual for how-to-use?
- In a plan, please be patient.

## I'm got issue with this library. Which place can I reproduce issue for you?
- If you found a issue, you can report this via [issue tab](https://github.com/dutwrapper/dutwrapper-java/issues) on this repository.

# Credit and license?
- License: [**MIT**](LICENSE)
- DISCLAIMER:
  - This project - dutwrapper - is not affiliated with [Da Nang University of Technology](http://sv.dut.udn.vn).
  - DUT, Da Nang University of Technology, web materials and web contents are trademarks and copyrights of [Da Nang University of Technology](http://sv.dut.udn.vn) school.
- Used third-party dependencies:
  - [Jsoup](https://github.com/jhy/jsoup): Licensed under the [MIT License](https://github.com/jhy/jsoup/blob/master/LICENSE).
  - [OkHttp3](https://github.com/square/okhttp): Licensed under the [Apache License 2.0](https://github.com/square/okhttp/blob/master/LICENSE.txt).
  - [Gson](https://github.com/google/gson): Licensed under the [Apache License 2.0](https://github.com/google/gson/blob/master/LICENSE).
