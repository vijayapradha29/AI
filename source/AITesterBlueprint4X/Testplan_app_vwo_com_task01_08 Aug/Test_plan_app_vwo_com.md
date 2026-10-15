# Test Plan Contents

**Application Under Test:** VWO (Wingify) — Login Page
**Application URL:** https://app.vwo.com/#/login
**Document ID:** TP-VWO-LOGIN-001 | **Version:** 1.0 | **Prepared By:** QA Engineer | **Date:** 15 October 2026 | **Status:** Draft

---

## 1. Test Plan Identifier

TP-VWO-LOGIN-001

## 2. Introduction / Overview

This test plan defines the objective, scope, approach, environment, resources, schedule and deliverables for testing the login page of the VWO (Wingify) application, reachable only at https://app.vwo.com/#/login. The page is a single login screen on which a user authenticates with an email address and password, and which also offers alternate sign-in options and supporting links. Every test item in this plan is limited to elements observed on that page; no other area of the application is covered.

## 3. Test Objectives

- Verify that a user can authenticate with a valid registered email address and password.
- Verify the behaviour of the login page when invalid, incomplete or malformed input is submitted.
- Verify that the email and password fields accept, mask and handle input as designed.
- Verify the alternate sign-in options (Google, SSO, Passkey) and the Forgot Password? control.
- Verify that the supporting links (Start a free trial, Privacy policy, Terms) resolve to their intended destinations.
- Verify the "Remember me" option behaves as intended.
- Identify, report, track and retest defects found on the login page.

## 4. Scope of Testing

**In Scope** — the elements observed on the login page:

- Email address field
- Password field
- Password visibility (show/hide) control
- Remember me option
- Forgot Password? control
- Sign in button
- Sign in with Google
- Sign in using SSO
- Sign in with Passkey
- "Start a free trial" link
- "Privacy policy" link
- "Terms" link
- On-page messaging presented by the login form
- Basic UI / layout of the login page

## 5. Out of Scope

- Everything beyond the login page: the post-login dashboard, account setup, campaigns, and any other authenticated area.
- User registration or account creation.
- Completion of the password-reset journey beyond the login page; only the login page's own Forgot Password? entry point is in scope.
- Performance, load, stress and security/penetration testing.
- The content of the external pages (wingify.com) that the links open.
- Repeated automated submission of fabricated credentials against this live production login.

## 6. Test Strategy / Approach

- **Testing type:** manual testing, performed in a real browser against the specified URL.
- **Testing level:** system testing of the login page.
- **Test design techniques:** positive, negative, boundary-value and edge-case testing.
- **Functional testing:** exercise each in-scope element and compare actual behaviour with expected behaviour.
- **UI testing:** verify the presence, labelling, order, masking and basic layout of the elements.
- **Regression testing:** re-run affected cases after any defect fix.
- **Defect management:** log defects with evidence, severity and priority; retest upon fix.

## 7. Test Items

| # | Test Item | Type | Observed on the login page |
|---|---|---|---|
| 1 | Email address field | Text input | id `login-username`, `type=email`, placeholder "Enter email ID", not marked required |
| 2 | Password field | Password input | id `login-password`, `type=password`, placeholder "Enter password", not marked required |
| 3 | Password visibility control | Button | toggles password visibility; labelled "Toggle password visibility" |
| 4 | Remember me | Checkbox | labelled "Remember me" |
| 5 | Forgot Password? | Button | text "Forgot Password?" |
| 6 | Sign in | Submit button | text "Sign in", `type=submit` |
| 7 | Sign in with Google | Button | text "Sign in with Google" |
| 8 | Sign in using SSO | Button | text "Sign in using SSO" |
| 9 | Sign in with Passkey | Button | text "Sign in with Passkey" |
| 10 | Start a free trial | Link | opens in a new tab (`target="_blank"`) |
| 11 | Privacy policy | Link | opens in a new tab (`target="_blank"`) |
| 12 | Terms | Link | opens in a new tab (`target="_blank"`) |
| 13 | Page heading and messaging | Text | heading "Sign in to Wingify platform"; "Don't have an account? Start a free trial"; "By continuing, you agree to Wingify's Privacy policy & Terms." |

## 8. Test Environment

- **Application URL:** https://app.vwo.com/#/login — the only environment used for this plan.
- **Application type:** web; a single login screen (page title observed: "Login - Wingify").
- **Browser:** Google Chrome.
- **Operating System:** Windows 11.
- **Network:** internet access to app.vwo.com is required.
- **Observed form configuration:** the login form carries HTML5 validation disabled (`novalidate`).
- **Test accounts:** a valid registered account is required for the successful-login case and must be supplied as authorised test data; no credentials are held in this document.

## 9. Test Data Requirements

- Valid registered email address with the correct password.
- Unregistered / unknown email address.
- Registered email address with an incorrect password.
- Empty email address and empty password (both fields blank).
- Valid email address with an empty password.
- Malformed email address (for example, one missing the "@" character).
- Email address with leading and/or trailing whitespace.
- Password values at boundary lengths (very short and very long) — note the password field enforces no `maxlength`.
- Non-ASCII and special-character values in the email and password fields.

Observed field attributes relevant to the data above: email field is `type=email`, password field is `type=password`; neither field carries a `required` attribute and neither enforces a `maxlength` limit.

## 10. Entry Criteria

- The login page at the specified URL is reachable.
- This test plan and the associated test cases are reviewed and approved.
- Authorised valid test-account credentials and the required negative test data are available.
- The target browser and operating system are available.
- The test environment information (URL, browser) is confirmed.

## 11. Exit Criteria

- All planned login-page test cases have been executed.
- Critical and high-severity defects are resolved or formally accepted.
- Failed test cases are documented with evidence.
- Retesting and any required regression testing are complete.
- Test execution results and a test summary report are prepared.

## 12. Test Deliverables

- Test Plan (this document)
- Test Scenarios
- Test Cases
- Test Data
- Test Execution Report
- Defect Reports
- Test Summary Report

## 13. Roles and Responsibilities

| Role | Responsibilities |
|---|---|
| QA Engineer | Prepare test scenarios and cases; execute tests on the login page; log and track defects; retest fixes; prepare reports. |
| Developer | Fix reported login-page defects; support defect analysis; provide fixes for retesting. |
| Business Analyst / Product Owner | Clarify expected login behaviour; confirm business expectations; support acceptance and sign-off. |

## 14. Test Schedule / Timeline

| Activity | Duration |
|---|---|
| Requirement analysis | 1 day |
| Test planning | 1 day |
| Test case design | 2 days |
| Test execution | 2 days |
| Defect retesting and regression | 1 day |
| Test summary and closure | 1 day |

**Total estimated duration:** 8 working days.

## 15. Defect Management

- Defects are logged in the designated defect-tracking tool with: ID, title, steps to reproduce, expected result, actual result, severity, priority, environment and supporting evidence.
- Each defect is assigned a severity and priority and routed to the appropriate developer.
- Every fix is retested, and regression testing is performed where the change could affect other login-page behaviour.
- Defects remain open until closed or formally accepted.
- Any security-sensitive observation (for example, handling of credential input) is recorded without exposing any actual credential value.

## 16. Risks and Mitigation

| Risk | Mitigation |
|---|---|
| Bot-detection / CAPTCHA may appear or block repeated sign-in attempts | Limit repeated submission attempts; if a control appears, record the case as blocked rather than attempting to bypass it. |
| Account lockout caused by repeated invalid logins | Use a dedicated test account and keep the number of invalid attempts minimal and ordered. |
| The target is an external, changing production page | Record the inspection date; re-verify element attributes if this plan is reused later. |
| Valid credentials unavailable | Mark the successful-login case as blocked rather than substituting fabricated credentials. |
| Network instability or page fails to load | Retry and confirm connectivity to app.vwo.com before execution. |
| Credential exposure | Supply credentials only through authorised test data; never store them in the plan, cases or reports. |

## 17. Resources and Tools

- Target application: https://app.vwo.com/#/login
- Browser: Google Chrome
- Operating System: Windows 11
- Defect tracking: designated defect-tracking tool
- Evidence: browser screenshots
- Communication: team communication channel / email

## 18. Approvals

| Role | Name | Approval Date | Status |
|---|---|---|---|
| QA Engineer | | | Pending |
| QA Lead | | | Pending |
| Project Manager / Product Owner | | | Pending |

**Document Status:** Draft — pending review and approval.
