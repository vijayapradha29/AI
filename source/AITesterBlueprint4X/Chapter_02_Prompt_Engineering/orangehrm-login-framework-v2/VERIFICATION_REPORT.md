# Verification Report — OrangeHRM Login (Python + Selenium + pytest + POM)

Generated: 2026-10-11
Framework: `Chapter_02_Prompt_Engineering/orangehrm-login-framework-v2/` (new, separate — the pre-existing
`orangehrm-login-framework/` was not modified)

Environment: Windows 11 (10.0.26200) | Python 3.12.6 (venv) | selenium 4.51.0 | pytest 9.1.1 | pytest-html 4.2.0 | Chrome 154.0.8037.100

Evidence labels: **CONFIRMED** = directly observed in a real browser / real run; **NOT FOUND** = actively searched
and absent; **Not verified** = not observed by execution.

## Required verification summary

| Field | Result |
|---|---|
| Planning completed | **Yes** — plan approved before any file was created |
| Browser launched (real Chrome) | **Yes** — Chrome 154.0.8037.100, headed for the primary run and the inspection |
| OrangeHRM page opened | **Yes** — title `OrangeHRM`, URL `https://opensource-demo.orangehrmlive.com/web/index.php/auth/login` |
| Live DOM inspected | **Yes** — login, empty-submit, invalid-submit, and post-login dashboard states captured |
| XPath locators verified against the live page | **Yes** — all six confirmed (table below) |
| Framework generated | **Yes** — 9 source/config files + this report |
| Test command, actual outcome | `python -m pytest` → **2 passed in 24.20s** (primary run) |
| Passed / failed / skipped | **2 / 0 / 0** (primary + headless); supplementary no-credential run **1 / 0 / 1 (skipped, BLOCKED)** |
| Report and evidence paths | see "Evidence paths" |
| Remaining blockers | on-failure screenshot hook untriggered; cross-browser not exercised |

## Locator verification (real Chrome, XPath only)

| Element | XPath | Status |
|---|---|---|
| Username field | `//input[@name='username']` | **CONFIRMED** — `type=text`, `name=username`, placeholder `Username`, displayed |
| Password field | `//input[@name='password']` | **CONFIRMED** — `type=password`, `name=password`, displayed |
| Login button | `//button[@type='submit']` | **CONFIRMED** — `type=submit`, text `Login`, displayed |
| Login error message | `//div[contains(@class,'oxd-alert')]//p` | **CONFIRMED** — appears after invalid submit; exact text `Invalid credentials` |
| Field validation message | `//span[contains(@class,'oxd-input-field-error-message')]` | **CONFIRMED** — appears after empty submit; text `Required` (2 elements) |
| Post-login indicator | `//h6[contains(@class,'oxd-topbar-header-breadcrumb-module')]` | **CONFIRMED** — text `Dashboard`, visible after login |

### Key structural finding — the app renders asynchronously

Immediately after `driver.get()`, none of the login fields are in the DOM; and immediately after the URL changes to
`/dashboard/index`, the topbar breadcrumb is still absent. Both appear only after an explicit wait. The framework
therefore performs every interaction through `WebDriverWait` (no fixed sleeps). Captured in `inspection.txt`:

```
ELEMENT username (immediately after get): NOT FOUND expected
WAIT OK (visible): username field
ELEMENT dashboardBreadcrumbImmediately: NOT FOUND
WAIT OK (visible): dashboard breadcrumb
```

### Invalid-login behaviour

After submitting invalid credentials the page stayed on `/web/index.php/auth/login` and displayed `Invalid credentials`.
The username/password fields remained displayed (still on the login page).

## Test execution (pytest, live)

**Primary run — `python -m pytest` (headed, public demo credentials via env):**

```
test_invalid_login.py::test_invalid_login PASSED   [ 50%]
test_valid_login.py::test_valid_login   PASSED     [100%]
============================= 2 passed in 24.20s ==============================
```

**Headless smoke run — `ORANGEHRM_HEADLESS=true python -m pytest`:**

```
test_invalid_login.py::test_invalid_login PASSED   [ 50%]
test_valid_login.py::test_valid_login   PASSED     [100%]
============================= 2 passed in 29.50s ==============================
```

**Credential-blocked path — `python -m pytest` with `ORANGEHRM_USERNAME`/`ORANGEHRM_PASSWORD` unset:**

```
test_invalid_login.py::test_invalid_login PASSED                              [ 50%]
test_valid_login.py::test_valid_login   SKIPPED (BLOCKED: valid creden...)    [100%]
SKIPPED [1] test_valid_login.py:1: BLOCKED: valid credentials not supplied
======================== 1 passed, 1 skipped in 22.30s ========================
```

Outcome classification: the four executed valid/invalid assertions are **genuine passes**. The single skip is an
intentional **BLOCKED** result (credentials withheld in that run), not a failure. No environment failure, authentication
restriction, or assertion failure occurred.

## Authentication / security controls

- Valid login used OrangeHRM's publicly documented **demo** credentials, injected only as environment variables at run
  time. No credential value is present in the source, the HTML report, the inspection logs, or this report.
- No MFA, CAPTCHA, or identity-verification challenge was encountered. If one had appeared, the run would have stopped
  and been reported (no bypass attempted).

## Evidence paths (absolute)

Project root: `C:\Users\Dhivya\source\AITesterBlueprint4X\Chapter_02_Prompt_Engineering\orangehrm-login-framework-v2`

- Primary HTML report: `...\reports\report.html`
- Headless HTML report: `...\reports\report-headless.html`
- Blocked-path HTML report: `...\reports\report-blocked.html`
- Login-page screenshot: `...\reports\inspection\login-page.png`
- Invalid-login screenshot: `...\reports\inspection\invalid-login.png`
- Post-login dashboard screenshot: `...\reports\inspection\dashboard.png`
- Inspection log: `...\reports\inspection\inspection.txt`
- Raw DOM captures: `...\reports\inspection\dom-login.html`, `dom-invalid.html`, `dom-dashboard.html`

## Files created (existing projects untouched)

```
Chapter_02_Prompt_Engineering/orangehrm-login-framework-v2/
├── config.py
├── driver_factory.py
├── conftest.py
├── pytest.ini
├── requirements.txt
├── .env.example
├── pages/
│   └── LoginPage.py
├── test_valid_login.py
├── test_invalid_login.py
├── VERIFICATION_REPORT.md
└── reports/
    ├── report.html
    ├── report-headless.html
    ├── report-blocked.html
    └── inspection/
        ├── login-page.png
        ├── invalid-login.png
        ├── dashboard.png
        ├── inspection.txt
        ├── dom-login.html
        ├── dom-invalid.html
        └── dom-dashboard.html
```

## Remaining blockers / not verified

- **On-failure screenshot** — the `pytest_runtest_makereport` hook is implemented and reachable, but no test failed, so
  it did not fire → **Not verified by execution**.
- **Empty-field validation assertions** — the `Required` messages were verified live during inspection, but the test
  scope is one invalid username/password combination, so they are not asserted by a test.
- **Cross-browser behaviour** — only Chrome was exercised; other browsers are **Not verified**.
- **Live DOM inspection capability** — inspection was performed with a Selenium-launched real Chrome (the approved
  fallback); no separate browser-inspection tool was used. If a dedicated inspection tool is required, it is **Not
  verified**.
