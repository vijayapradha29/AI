# Verification Report — OrangeHRM Login (Python + Selenium + pytest + POM)

Generated: 2026-10-10
Host: Windows 11 (10.0.26200) | Python 3.12.6 | selenium 4.32.0 | pytest 8.4.1 | pytest-html 4.1.1 | Chrome 154.0.8037.100

Evidence labels: **CONFIRMED** = directly observed in a real browser / real run; **NOT FOUND** = actively searched and absent; **UNVERIFIED / Not verified** = not observed.

## Summary

| Field | Result |
|---|---|
| Planning completed | **Yes** — plan approved before any file was created |
| Browser launched (real Chrome) | **Yes** — Chrome 154.0.8037.100, headed for the primary run |
| OrangeHRM page opened | **Yes** — title `OrangeHRM`, URL `https://opensource-demo.orangehrmlive.com/web/index.php/auth/login` |
| Live DOM inspected (post-JS) | **Yes** — login, invalid-submit, and post-login dashboard DOM captured |
| XPath locators verified against the live page | **Yes** — see locator table |
| Framework generated | **Yes** — 9 source/config files (see tree) |
| Test command executed and outcome | `python -m pytest` → **2 passed in 23.55s** (primary run) |
| Tests passed / failed / skipped | **2 passed / 0 failed / 0 skipped** (primary run); supplementary run **1 passed / 0 failed / 1 skipped (BLOCKED)** |
| Evidence location | `...\orangehrm-login-framework\reports\` (report + inspection screenshots/logs) |
| Remaining blockers | Allure report not generated; on-failure screenshot hook untriggered; `agent-browser` inspection capability unavailable |

## Locator verification (real browser, XPath only)

| Element | XPath | Status |
|---|---|---|
| Username field | `//input[@name='username']` | **CONFIRMED** — `type=text`, `name=username`, placeholder `Username`, displayed |
| Password field | `//input[@name='password']` | **CONFIRMED** — `type=password`, `name=password`, displayed |
| Login button | `//button[@type='submit']` | **CONFIRMED** — `type=submit`, text `Login`, displayed |
| Login error message | `//div[contains(@class,'oxd-alert')]//p` | **CONFIRMED** — appears after invalid submit; exact text `Invalid credentials` |
| Field validation message | `//span[contains(@class,'oxd-input-field-error-message')]` | **CONFIRMED** — appears after empty submit; text `Required` (2 elements) |
| Post-login indicator (module) | `//h6[contains(@class,'oxd-topbar-header-breadcrumb-module')]` | **CONFIRMED** — text `Dashboard`, visible after login |
| Post-login indicator (user) | `//p[contains(@class,'oxd-userdropdown-name')]` | **CONFIRMED** — text `Bilol Abdurasul`, visible after login |
| "Remember Me" checkbox | `//input[@type='checkbox']` | **NOT FOUND** — the page has no checkbox (not requested by the OrangeHRM spec) |

### Key structural finding — the post-login topbar loads asynchronously

Immediately after the URL changed to `/dashboard/index`, the topbar elements were **NOT FOUND**; they became visible
only after an explicit wait. The framework therefore waits for the breadcrumb element rather than asserting on the URL
alone. Verified by re-inspection:

```
ELEMENT dash: NOT FOUND -> //p[contains(@class,'oxd-userdropdown-name')]      (before wait)
WAIT OK (visible): //p[contains(@class,'oxd-userdropdown-name')]              (after wait)
ELEMENT dash-after-wait[0]: tag=p ... text='Bilol Abdurasul'
ELEMENT dash-after-wait[0]: tag=h6 ... text='Dashboard'
```

### Invalid-login behaviour

After submitting invalid credentials the page stayed on `/web/index.php/auth/login` and displayed
`Invalid credentials` in `//div[contains(@class,'oxd-alert')]//p`. The username/password fields remained displayed.

## Test execution (pytest, live)

**Primary run — `python -m pytest` (headed, authorized demo credentials):**

```
test_invalid_login.py::test_invalid_login PASSED   [ 50%]
test_valid_login.py::test_valid_login   PASSED     [100%]
============================= 2 passed in 23.55s ==============================
```

**Supplementary run — `ORANGEHRM_HEADLESS=true`, no credentials (verifies headless mode + blocked path):**

```
test_invalid_login.py::test_invalid_login PASSED                              [ 50%]
test_valid_login.py::test_valid_login   SKIPPED (BLOCKED: valid creden...)    [100%]
SKIPPED [1] test_valid_login.py:1: BLOCKED: valid credentials not supplied
======================== 1 passed, 1 skipped in 33.95s ========================
```

Chrome opening and navigation: **confirmed** — the tests can only pass after a real Chrome session navigates to
OrangeHRM and performs DOM interaction; both runs produced passing tests and were corroborated by the DOM captures and
screenshots below.

Classification of outcomes: both primary tests are **genuine passes** (executed and asserted). No environment failure,
authentication restriction, locator failure, or assertion failure occurred. The single skip is an intentional
**BLOCKED** result (no credentials supplied in that run), not a failure.

## Authentication / security controls

- Valid login used OrangeHRM's publicly documented **demo** credentials, injected via environment variables.
- No credential value is present in the source, the HTML report, or this report.
- No MFA, CAPTCHA, or identity-verification challenge was encountered during any run.
- Credential-submitting tests are authorized for this demo target; the valid-login test self-reports **BLOCKED**
  (skip) when credentials are absent rather than inventing them.

## Evidence paths (absolute)

Project root: `C:\Users\Dhivya\source\AITesterBlueprint4X\Chapter_02_Prompt_Engineering\orangehrm-login-framework`

- pytest HTML report: `...\orangehrm-login-framework\reports\report.html`
- Login-page screenshot: `...\reports\inspection\login-page.png`
- Invalid-login screenshot: `...\reports\inspection\invalid-login.png`
- Post-login dashboard screenshot: `...\reports\inspection\dashboard.png`
- Inspection log (login / empty / invalid): `...\reports\inspection\phase2-inspection.txt`
- Inspection log (post-login indicator): `...\reports\inspection\phase2-dashboard-inspection.txt`
- Raw DOM capture (scratchpad, session-scoped): `...\scratchpad\orangehrm-dom-login.html`,
  `...\orangehrm-dom-invalid.html`, `...\orangehrm-dom-dashboard2.html`

## Files created (existing Java projects untouched)

```
Chapter_02_Prompt_Engineering/orangehrm-login-framework/
├── config.py
├── driver_factory.py
├── conftest.py
├── pytest.ini
├── requirements.txt
├── pages/
│   └── LoginPage.py
├── test_valid_login.py
├── test_invalid_login.py
├── VERIFICATION_REPORT.md
└── reports/
    ├── report.html
    └── inspection/
        ├── login-page.png
        ├── invalid-login.png
        ├── dashboard.png
        ├── phase2-inspection.txt
        └── phase2-dashboard-inspection.txt
```

## Remaining blockers / not verified

- **Allure report** — the `allure-pytest` plugin is installed, but the Allure CLI is **not usable** on this host
  (`ERROR: JAVA_HOME is set to an invalid directory`). No Allure report was generated → **Not verified**. pytest-html
  was used instead (as selected).
- **On-failure screenshot** — the hook is implemented and reachable, but no test failed, so it did not fire →
  **Not verified by execution**.
- **`agent-browser` live-inspection tool** — not installed (`agent-browser` is not recognized); live DOM inspection was
  performed with the approved Selenium fallback. The agent-browser capability is **Not verified**.
- **Empty-field validation assertions** — the `Required` messages were verified live, but per the "one invalid
  username/password combination" test scope they are not asserted by a test.
- **Sustained load / cross-browser behaviour** — only Chrome was exercised; other browsers are **Not verified**.
