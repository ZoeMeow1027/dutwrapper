# DUTWRAPPER CHANGE LOG

This file will list all version log for modified, added or removed functions of dutwrapper wiht Java language.

## 1.11.2
- [New] Form requested (Lập đơn xác nhận)
  - You will need to login with your account to use that.
- [Implemented] Updated Gradle to 8.10.2 and all dependencies to latest.

## 1.11.1-patch1
- [Changed] Changed `StandardCharsets.UTF_8.toString()` to `"UTF-8"` to avoid error in Android.

## 1.11.1
- [Fixed] Fixed issue cause duplicate re-study subject results.
- [Changed] Property `id` in `AccountInformation.SubjectResult` changed from ~~`String`~~ to `AccountInformation.SubjectCode`.

## 1.11.0
- [Changed] Merge all class in `model` package to `AccountInformation` class.
- [Changed] Moving `Account` class functions to `Accounts` class and this will return each data type in `AccountInformation` class.
- [Changed] Change some properties in `AccountInfrormation` class (merged from whole class in `model` package). You'll need to modify your code to working again.
- [Implemented] Optimize codes.

## 1.10.2
- [Changed] Merge all class related to News into once.
- [Changed] Merge all class related to Utils into once.
- [Changed] SetPrintStackTrace in HttpClientWrapper has been moved to `Variables` class.
- [Implemented] Updated Gradle to 8.9 and all dependencies to latest.

## 1.10.1
- [New] We're trying to sync properties serialize name between Java, Net, Python.
- [Changed] Updated dependencies to latest.
- [Changed] Removed `NewsGlobalGroupByDate` and `NewsSubjectGroupByDate`.
- [Fixed] `java.net.UnknownHostException` was thrown even you include them in try-catch.

## 1.10.0
- `Account.Session` class is now in part of Account class.
  - This will ensure you can get `__VIEWSTATE` automatically to session (just use `Account.Session`, that's it).
  - `Account.getSession()` is everything you need.
- `login()` and `logout()` will now `void` instead of `boolean`.
  - This will reduce `isLoggedIn` function to avoid extra requests.
  - You will need to check session manually with `isLoggedIn`.
- `GraduateStatus` class:
  - `info1` will now `rewardsInfo`.
  - `info2` will now `discipline`.
  - `info3` will now `eligibleGraduationThesisStatus`.
  - `approveGraduateProcessInfo` will now `eligibleGraduationStatus`.

## 1.9.2
- Fixed cannot login account using this library.
  - Sv.dut.udn.vn seems to reboot server again, so __VIEWSTATE changed again.

## 1.9.1
- Fixed issue cause SubjectCodeItem parses failed (by change all variables to string).
- Fixed issue cause AccountTrainingStatus parses wrong data.

## 1.9.0
- Move all HttpClientWrapper to project root (customrequest2 to HttpClientWrapper).
- Fixed issues causes Account class not working.
- Updated all dependencies to latest.

## 1.8.4
- Update dependencies to latest.
- `NewsGroupByDate` is now interface. Use `NewsGlobalGroupByDate` and `NewsSubjectGroupByDate` for initializing group.

## 1.8.3
- Fixed a issue cause news sorted by descending in get news group by date.
- Fixed a issue cause subject code item throw a exception about string format.

## 1.8.2
- Add a option for get news to group by date. You can manually access them by two function in News class:
  - getNewsGlobalGroupByDate
  - getNewsSubjectGroupByDate
- Merge two news test into once.

## 1.8.1
- Solved a mistake in SubjectResult constructor.
- Updated dependencies to latest version.

## 1.8.0
- Changed package name and folder structure to io.dutwrapper.dutwrapper.
- Added getAccountTrainingStatus() from Account class.
- Removed DutScheduleWeek in Utils, which is replaced by getCurrentSchoolWeek().

## 1.7.5
- Changed package name to io.dutwrapperlib.dutwrapper. Get package in JitPack still is io.dutwrapper-lib.java.

## 1.7.4
- Fixed a issue which in web fault cause news subject failed to fetch data if different split condition is used (ex: 20Nh92 instead of 20.Nh92).

## 1.7.3
- Optimize code performance.
- **[NOTE]:** You can use 1.7.2-hotfix1 if your code ran properly.

## 1.7.2-hotfix1
- Instead of load file, directly save json to Variables.java.

## 1.7.2
- An issue is included a fix, but not testing: getDUTSchoolYear() in Utils.
- Added lecturer name and lecturer gender in NewsSubjectItem, however, this is still in alpha.

## 1.7.1 (Hotfix for 1.7.0)
- Fixed an issue in SubjectCodeItem cause string parameter in constructor isn't working.

## 1.7.0
- Updated implementations to latest.
- Update features in News Subject. More details please view code [here](src\main\java\io\zoemeow\dutapi\News.java).
- Add a function to get current DUT week.

## 1.6.3

- Extended timeout limit to 60 seconds.
- Fixed issues and optimize codes.

## 1.6.2

- JitPack.io support for this library.
- Fixed issues and optimize codes.

## 1.6.1

- Fix critical error.

## 1.6.0

- Move from VS Code to Intellij IDEA.
- Migrate with old commits.
- Rename from Session to Accounts and Major changes for Accounts.
- Used OkHttp3.

## 1.5.1

- Switched to Gradle.

## 1.5

- Chaged variables for Subject Schedue.
- Schedule Study and Schedule Exam will be independence.

## 1.4.2

[.NET]

- Optimize code line.

## 1.4.1

[.NET]

- Merge GetNewsGeneral() and GetNewsSubject() together.
    - To GetNews().

## 1.4

- Inital commit for Java.
- Move trello schedule link to [DEVPLANNING.md](DEVPLANNING.md).

## 1.3

- Inital commit for Python.
- Delete unneed files for .NET.
- Optimize code :)

## 1.2

- Added feature: Get all subjects fee list.
- Change file and function name: ~~GetScheduleSubject~~ to **GetSubjectsSchedule**.
- Add [CHANGELOG.md](CHANGELOG.md) for all old logs.
- Optimize code.

## 1.1

- Added feature: Get schedule about subjects and examimation.

## 1.0

Inital commit with features:

- Get news general (Nhận thông báo chung).
- Get news subjects (Nhận thông báo lớp học phần).
- Sesion (Phiên, dùng để đăng nhập/đăng xuất/lấy thông tin tài khoản).
