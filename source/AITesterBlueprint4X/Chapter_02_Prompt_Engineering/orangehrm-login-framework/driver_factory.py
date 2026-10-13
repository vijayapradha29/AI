from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait

import config


def _chrome_options():
    options = Options()
    options.add_argument("--start-maximized")
    options.add_argument("--window-size=1920,1080")
    options.add_argument("--disable-notifications")
    options.add_argument("--disable-infobars")
    options.add_experimental_option("excludeSwitches", ["enable-logging"])
    if config.HEADLESS:
        options.add_argument("--headless=new")
    return options


def create_driver():
    if config.BROWSER != "chrome":
        raise ValueError(
            f"Unsupported browser configured: {config.BROWSER!r}. Only 'chrome' is supported."
        )
    try:
        driver = webdriver.Chrome(options=_chrome_options())
    except Exception as exc:
        raise RuntimeError(f"Failed to start the Chrome WebDriver session: {exc}") from exc
    driver.set_page_load_timeout(config.PAGE_LOAD_TIMEOUT)
    return driver


def wait_for(driver, timeout=None):
    return WebDriverWait(driver, config.EXPLICIT_WAIT if timeout is None else timeout)


def quit_driver(driver):
    if driver is None:
        return
    try:
        driver.quit()
    except Exception as exc:
        raise RuntimeError(f"Failed to close the Chrome WebDriver session cleanly: {exc}") from exc
