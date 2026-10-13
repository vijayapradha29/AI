def test_valid_login(login_page, valid_credentials):
    username, password = valid_credentials

    assert login_page.is_username_displayed(), "Username field is not displayed on the login page"
    assert login_page.is_password_displayed(), "Password field is not displayed on the login page"
    assert login_page.is_login_button_displayed(), "Login button is not displayed on the login page"

    login_page.login(username, password)

    assert login_page.is_authenticated(), (
        "User was not authenticated after a valid login. "
        f"Current URL: {login_page.get_current_url()}"
    )
    assert login_page.get_dashboard_module_text() == "Dashboard", (
        "The post-login dashboard breadcrumb did not show 'Dashboard'"
    )
