# Plan: Salesforce Login — Enterprise Selenium + Java + Maven + TestNG Framework

## What I understand

Your prompt is a **RICE POT** exercise (Role/Instructions/Context/Example/Parameters/Output/Tone) that mirrors
`Chapter_02_Prompt_Engineering/00_Task1.md` — *"Write a Selenium Code for the Sales Force login"*. The deliverable is a
runnable, enterprise-grade automation framework that tests the Salesforce login page
(`https://login.salesforce.com/?locale=in`) with **1 Page Object**, **2 TestNG test scripts** (valid + invalid), driven by a
**Maven** project.

Corrections I am applying (per this repo's anti-hallucination rules):
- Salesforce login uses a **username**, not an email (the prompt's Context mentions "email" — corrected to username).
- The prompt's Context mentions "AB Testing website" — that is not Salesforce and is ignored.
- All locators use **xpath only** (no CSS / id / name / linkText selectors). Zero comments. No `Thread.sleep()`.
- Salesforce element facts used (well-known login-form IDs, expressed as xpath): username=`//input[@id='username']`,
  password=`//input[@id='password']`, login=`//input[@id='Login']`, remember-me=`//input[@id='rememberUn']`,
  error=`//div[@id='error']`. Success is asserted by URL change / absence of the error element (avoid asserting an uncertain
  Lightning/classic home element).

### Decisions (confirmed)
- **Location:** `Chapter_02_Prompt_Engineering/salesforce-login-framework/` (keeps Task1 prompt + output together).
- **Scope:** Lean enterprise — everything needed to actually run, nothing more.
- **Credentials:** externalized to `config.properties` placeholders + `-D`/env override; invalid tests use fabricated data.
- **Browser:** Chrome via Selenium 4 **Selenium Manager** (zero driver setup), `headless` toggled via config.

## Project structure

```
Chapter_02_Prompt_Engineering/salesforce-login-framework/
├── pom.xml
├── testng.xml
└── src/
    ├── main/java/com/salesforce/framework/
    │   ├── config/ConfigReader.java        # loads config.properties, -D/env overrides
    │   ├── driver/DriverFactory.java       # ThreadLocal WebDriver, Chrome + Selenium Manager
    │   └── pages/LoginPage.java            # THE Page Object (PageFactory, xpath-only)
    └── test/
        ├── java/com/salesforce/framework/base/BaseTest.java
        ├── java/com/salesforce/tests/ValidLoginTest.java      # script 1
        ├── java/com/salesforce/tests/InvalidLoginTest.java    # script 2
        └── resources/config.properties
```

Rationale: pages + framework live in `src/main/java` (reusable library); test infra + tests in `src/test/java`.

## Files & responsibilities

### 1. `pom.xml`
- `groupId=com.salesforce`, `artifactId=salesforce-login-framework`, `version=1.0.0`, `packaging=jar`.
- Java 17 (`maven.compiler.release=17`), UTF-8 source encoding.
- Dependencies: `org.seleniumhq.selenium:selenium-java:4.25.0`, `org.testng:testng:7.10.2`,
  `org.slf4j:slf4j-simple:2.0.13` (silences driver logs).
- Build: `maven-compiler-plugin:3.13.0`; `maven-surefire-plugin:3.2.5` with `<suiteXmlFiles><suiteXmlFile>testng.xml</suiteXmlFile>`.

### 2. `src/test/resources/config.properties`
```
baseUrl=https://login.salesforce.com/?locale=in
browser=chrome
headless=false
implicitWait=10
explicitWait=20
validUsername=REPLACE_WITH_VALID_USERNAME
validPassword=REPLACE_WITH_VALID_PASSWORD
```

### 3. `ConfigReader.java`
- Loads `config.properties` from classpath once (static, `Properties`).
- `get(String)`, `getInt(String)`, `getBoolean(String)`; **system property / env var takes precedence** over the file so
  creds can be injected with `-DvalidUsername=...`.
- Missing key → `RuntimeException` with the key name in the message. Wrapped in try-catch.

### 4. `DriverFactory.java`
- `ThreadLocal<WebDriver>` for parallel-safety.
- `createDriver()`: builds `ChromeOptions` (`--start-maximized`, `--disable-notifications`, `--headless=new` when
  `headless=true`), returns `new ChromeDriver(options)` (Selenium Manager resolves the driver).
- Sets implicit wait from `implicitWait`; `getDriver()` / `quitDriver()` (null-safe, try-catch → `RuntimeException`).

### 5. `LoginPage.java` (the Page Object — `LoginPage(WebDriver)` + `PageFactory.initElements`)
- `@FindBy(xpath = ...)` fields for username, password, login button, remember-me, error message.
- Ctor: `this.driver = driver; PageFactory.initElements(driver, this);`
- Reusable action methods, each wrapping interaction in `WebDriverWait` (`explicitWait`) + **structured try-catch**
  (rethrow as `RuntimeException` with context, or return boolean):
  - `typeUsername(String)`, `typePassword(String)`, `clickLogin()`, `login(String,String)`, `checkRememberMe()`
  - `getErrorMessage()` (waits for visibility, returns text), `isErrorMessageDisplayed()`
  - `isUsernameFieldDisplayed()`, `isPasswordFieldDisplayed()`, `isLoginButtonDisplayed()`, `isRememberMeDisplayed()`
  - `getCurrentUrl()`
- Every method returns `LoginPage` for chaining where sensible; no comments, xpath only, no `Thread.sleep`.

### 6. `BaseTest.java`
- `@BeforeTest setUp()` — suite-level config load.
- `@BeforeMethod initDriver()` — `driver = DriverFactory.createDriver(); driver.get(ConfigReader.get("baseUrl"));`
- `@AfterMethod tearDown()` — `DriverFactory.quitDriver();`
- `getDriver()` accessor. Setup/teardown wrapped in try-catch so teardown always runs.

### 7. `ValidLoginTest.java` (script 1 — valid + UI)
- `@DataProvider validCredentials()` reading `validUsername` / `validPassword`.
- `@Test(priority=1) verifyLoginPageUiElements()` — username, password, login button, remember-me all displayed.
- `@Test(dataProvider="validCredentials", priority=2) verifyValidLogin()` — enter creds, click login, assert
  `!getCurrentUrl().contains("login.salesforce.com")` and error message not displayed.

### 8. `InvalidLoginTest.java` (script 2 — invalid)
- `@DataProvider invalidCredentials()` with rows: wrong password, invalid username, empty username, empty password, both empty.
- `@Test(dataProvider="invalidCredentials") verifyInvalidLogin(...)` — assert error message displayed, expected copy
  present (e.g. "Please check your username and password..."), and URL still on `login.salesforce.com`.
- All asserts via TestNG `Assert`; page calls wrapped in try-catch where they can throw.

### 9. `testng.xml`
- Suite `SalesforceLoginSuite`, two `<test>` blocks (`ValidLogin`, `InvalidLogin`) each referencing its class.

## Constraints checklist (enforced on every file)
- [x] xpath-only `@FindBy` — no CSS/id/name/linkText
- [x] Page Object + `PageFactory.initElements` + reusable action methods
- [x] `@Test`, `@BeforeTest`, `@BeforeMethod`, `@AfterMethod` + setup/teardown
- [x] Robust exception handling (try-catch / explicit signatures) in page + test code
- [x] **Zero comments**, no `Thread.sleep()` — `WebDriverWait`/implicit waits only
- [x] Runnable, modular, consistent structure

## Verification
From `Chapter_02_Prompt_Engineering/salesforce-login-framework/`:
1. `mvn -q clean test -DvalidUsername=<real> -DvalidPassword=<real>` — full suite.
2. `mvn -q test -Dheadless=true` — headless run.
3. Invalid suite only: `mvn -q test -Dtest=InvalidLoginTest` — should pass with fabricated data (no creds needed).
4. Valid suite needs real external/staging creds; if the org enforces MFA/verification the valid test will fail by design —
   the URL-based assertion is deliberately tolerant, and MFA should be disabled on the test org.

## Risks / notes
- The `validLogin` assertion avoids claiming a specific post-login Lightning/classic element (uncertain) and instead checks
  navigation away from the login page + absence of the error element.
- `REPLACE_WITH_...` placeholders are intentional; supply real creds via `-D` or by editing `config.properties` (never commit real creds).
- No README added — the prompt forbids extra text/non-code output.
