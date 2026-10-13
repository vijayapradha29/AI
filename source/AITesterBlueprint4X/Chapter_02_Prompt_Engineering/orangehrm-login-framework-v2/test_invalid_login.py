INVALID_USERNAME = "invalid_user"
INVALID_PASSWORD = "invalid_password"


def test_invalid_login(login_page):
    assert login_page.is_username_displayed(), "Username field is not displayed on the login page"
    assert login_page.is_password_displayed(), "Password field is not displayed on the login page"
    assert login_page.is_login_button_displayed(), "Login button is not displayed on the login page"

    login_page.login(INVALID_USERNAME, INVALID_PASSWORD)

    error_message = login_page.get_error_message()
    assert error_message == "Invalid credentials", (
        f"Unexpected authentication error text: {error_message!r}"
    )
    assert "dashboard/index" not in login_page.get_current_url(), (
        "User was redirected to the dashboard after an invalid login attempt"
    )
    assert login_page.is_on_login_page(), (
        "Login page is no longer displayed after an invalid login attempt"
    )
