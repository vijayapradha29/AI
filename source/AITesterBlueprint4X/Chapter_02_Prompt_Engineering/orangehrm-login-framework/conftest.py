import os
from datetime import datetime

import pytest

import config
from driver_factory import create_driver, quit_driver, wait_for
from pages.LoginPage import LoginPage


@pytest.fixture()
def driver():
    web_driver = create_driver()
    try:
        yield web_driver
    finally:
        quit_driver(web_driver)


@pytest.fixture()
def login_page(driver):
    page = LoginPage(driver, wait_for(driver))
    page.open(config.BASE_URL)
    return page


@pytest.fixture()
def valid_credentials():
    username, password = config.get_credentials()
    if not username or not password:
        pytest.skip(
            "BLOCKED: valid credentials not supplied "
            f"(set the {config.USERNAME_ENV} and {config.PASSWORD_ENV} environment variables)"
        )
    return username, password


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    report = outcome.get_result()
    if report.when != "call" or not report.failed:
        return
    web_driver = item.funcargs.get("driver")
    if web_driver is None:
        return
    os.makedirs(config.SCREENSHOT_DIR, exist_ok=True)
    safe_node_id = (
        item.nodeid.replace("::", "_").replace("/", "_").replace("\\", "_").replace(" ", "_")
    )
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    path = os.path.join(config.SCREENSHOT_DIR, f"{safe_node_id}_{timestamp}.png")
    try:
        web_driver.save_screenshot(path)
    except Exception as exc:
        report.sections.append(("screenshot-error", f"Could not capture a screenshot: {exc}"))
