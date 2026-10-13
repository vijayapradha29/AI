# Verification Report — Salesforce Login (Experimental Framework)

Generated: 2026-10-09
Host: Windows 11 (amd64) | Chrome 154.0.8037.100 | JDK 23.0.1 | Apache Maven 3.9.9

Evidence labels: **CONFIRMED** = directly observed in a real browser / real run; **UNVERIFIED** = not observed; **NOT FOUND** = actively searched and absent.

## Summary

| Field | Result |
|---|---|
| Planning completed | Yes |
| Browser launched (real Chrome) | Yes — Chrome 154.0.8037.100, headed session |
| Salesforce page opened | Yes — title `Login \| Salesforce`, URL `https://login.salesforce.com/?locale=in` |
| Live DOM inspected (post-JS) | Yes — 38 `<input>` elements enumerated from the rendered DOM |
| XPath locators verified against live page | Partial — see locator table |
| Framework generated | Yes |
| Maven compilation result | BUILD SUCCESS (3 main + 3 test sources, `release 17`) |
| Tests passed / failed / skipped | **2 passed / 0 failed / 3 skipped** (5 total) |
| Evidence location | `Chapter_02_Prompt_Engineering\salesforce-login-experimental\target\surefire-reports\` |
| Remaining blockers | Valid login, password-stage locator, and the server-side invalid-credential error are **Not verified** |

## Locator verification (real browser)

| Element | XPath | Status |
|---|---|---|
| Username | `//input[@id='username']` | **CONFIRMED** — `type=email`, `name=username` |
| Login button | `//input[@id='Login']` | **CONFIRMED** — `type=submit`, `value="Log In"` |
| Remember Me | `//input[@id='rememberUn']` | **CONFIRMED** — `type=checkbox` |
| Login error message | `//div[@id='error']` | **CONFIRMED** — appears after submit; text `Error: Please enter your username.` |
| Forgot password | `//a[@id='forgot_password_link']` | **CONFIRMED** — displayed |
| Log In with Email | `//a[@id='gidr-email-log-in-button']` | **CONFIRMED** — displayed |
| Password field | `//input[@id='password']` | **NOT FOUND on the first step** — see below; locator **UNVERIFIED** |

### Key structural finding — two-step (username-first) login

The rendered page contains **no password field at all**. Search of the captured DOM found only the hidden
`name="passwordShown"` field and the "Forgot Your Password?" link — no `input[@type='password']`, no `id="password"`,
no `name="pw"`. The observed flow is therefore:

1. Enter username → click `Log In` (server round-trip).
2. The password step is served afterwards.

The password-stage locator (`//input[@id='password']`) could only be confirmed by submitting a username, which was
**not authorized**, so it remains **UNVERIFIED**. No username/password was submitted.

## Test execution (Maven + TestNG, live)

| Test | Result | Notes |
|---|---|---|
| `ValidLoginTest.verifyLoginPageUiElements` | **PASS** (256 ms) | username / Login / Remember Me displayed |
| `ValidLoginTest.verifyValidLogin` | **SKIP** | No valid credentials supplied |
| `InvalidLoginTest.verifyEmptyUsernameShowsValidation` | **PASS** (1.4 s) | Live error text contained `Please enter your username` |
| `InvalidLoginTest.verifyInvalidCredentials` (2 rows) | **SKIP** | Credential-submitting tests not authorized |

Totals: `Tests run: 5, Failures: 0, Errors: 0, Skipped: 3` — BUILD SUCCESS.

Run scopes used:
- `mvn clean test` — full suite (credential-submitting tests self-skip unless enabled).

Credential-submitting tests are gated:
- `verifyValidLogin` skips unless `-DvalidUsername` / `-DvalidPassword` are supplied.
- `verifyInvalidCredentials` skips unless `-DallowCredentialTests=true` is supplied.

## Authentication / security controls

- No live credential was entered or submitted. Only the **empty-field validation** round-trip was exercised
  (no username, no password).
- No MFA, CAPTCHA, or identity-verification challenge was presented or encountered on this page.
- Valid-login credentials are deliberately **not** passed through a TestNG DataProvider, so no secret is echoed into
  reports; the value is read inside the test only.

## Evidence paths (absolute)

- Surefire summary: `C:\Users\Dhivya\source\AITesterBlueprint4X\Chapter_02_Prompt_Engineering\salesforce-login-experimental\target\surefire-reports\TestSuite.txt`
- Surefire XML: `...\target\surefire-reports\TEST-TestSuite.xml`
- TestNG results: `...\target\surefire-reports\testng-results.xml`
- HTML report: `...\target\surefire-reports\emailable-report.html`
- Live DOM capture (scratchpad, session-scoped): `C:\Users\Dhivya\AppData\Local\Temp\commandcode\C--Users-Dhivya-source-AITesterBlueprint4X\2e93870e-6f18-46d4-afcf-8d5e2601c4d1\scratchpad\inspect\login-dom.html`
- Inspection log: `...\scratchpad\inspect\phase2-inspection.txt`
- Maven used for the run: `...\scratchpad\tools\apache-maven-3.9.9\bin\mvn.cmd` (JAVA_HOME=`C:\Program Files\Java\jdk-23`)

## Remaining blockers / not verified

- **Password-stage locator** — UNVERIFIED (requires submitting a username; not authorized).
- **Valid login + post-login indicator** — UNVERIFIED (no authorized credentials supplied). `isAuthenticated()`
  requires a URL change *and* a post-login element, so a URL change alone is never accepted.
- **Server-side invalid-credential error text** — UNVERIFIED (credential-submitting tests not authorized).
- **Screenshots** — none produced; no test failed.
- **Benign warning** — Selenium 4.25 emits `Unable to find CDP implementation matching 154` because Chrome 154 is
  newer than the bundled CDP bindings. It does not affect element interaction; no CDP features are used.
- **Environment note** — Maven is not installed on this machine; it was provisioned in the session scratchpad for
  these runs. The project's `pom.xml` is standard and runs with any Maven 3.9+.

## Files created (existing `salesforce-login-framework/` untouched)

```
Chapter_02_Prompt_Engineering/salesforce-login-experimental/
├── pom.xml
├── testng.xml
├── VERIFICATION_REPORT.md
└── src/
    ├── main/java/com/salesforce/framework/
    │   ├── config/ConfigReader.java
    │   ├── driver/DriverFactory.java
    │   └── pages/LoginPage.java
    └── test/
        ├── java/com/salesforce/framework/base/BaseTest.java
        ├── java/com/salesforce/tests/ValidLoginTest.java
        ├── java/com/salesforce/tests/InvalidLoginTest.java
        └── resources/config.properties
```
