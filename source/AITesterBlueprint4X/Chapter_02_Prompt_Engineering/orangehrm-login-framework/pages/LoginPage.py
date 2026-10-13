from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait


class LoginPage:
    USERNAME_INPUT = (By.XPATH, "//input[@name='username']")
    PASSWORD_INPUT = (By.XPATH, "//input[@name='password']")
    LOGIN_BUTTON = (By.XPATH, "//button[@type='submit']")
    AUTH_ERROR = (By.XPATH, "//div[contains(@class,'oxd-alert')]//p")
    FIELD_ERROR = (By.XPATH, "//span[contains(@class,'oxd-input-field-error-message')]")
    DASHBOARD_BREADCRUMB = (
        By.XPATH,
        "//h6[contains(@class,'oxd-topbar-header-breadcrumb-module')]",
    )
    USER_DROPDOWN_NAME = (By.XPATH, "//p[contains(@class,'oxd-userdropdown-name')]")

    def __init__(self, driver, wait):
        self.driver = driver
        self.wait = wait

    def _wait(self, timeout=None):
        return self.wait if timeout is None else WebDriverWait(self.driver, timeout)

    def _visible(self, locator, timeout=None):
        try:
            return self._wait(timeout).until(EC.visibility_of_element_located(locator))
        except TimeoutException as exc:
            raise RuntimeError(f"Element was not visible within the timeout: {locator}") from exc

    def _clickable(self, locator, timeout=None):
        try:
            return self._wait(timeout).until(EC.element_to_be_clickable(locator))
        except TimeoutException as exc:
            raise RuntimeError(f"Element was not clickable within the timeout: {locator}") from exc

    def _is_displayed(self, locator, timeout=None):
        try:
            return self._visible(locator, timeout).is_displayed()
        except RuntimeError:
            return False

    def open(self, url):
        try:
            self.driver.get(url)
        except TimeoutException as exc:
            raise RuntimeError(f"Timed out while loading the URL: {url}") from exc
        except Exception as exc:
            raise RuntimeError(f"Failed to open the URL: {url}") from exc
        return self

    def enter_username(self, username):
        try:
            field = self._visible(self.USERNAME_INPUT)
            field.clear()
            field.send_keys(username)
        except RuntimeError:
            raise
        except Exception as exc:
            raise RuntimeError("Failed to enter the username into the username field") from exc
        return self

    def enter_password(self, password):
        try:
            field = self._visible(self.PASSWORD_INPUT)
            field.clear()
            field.send_keys(password)
        except RuntimeError:
            raise
        except Exception as exc:
            raise RuntimeError("Failed to enter the password into the password field") from exc
        return self

    def click_login(self):
        try:
            self._clickable(self.LOGIN_BUTTON).click()
        except RuntimeError:
            raise
        except Exception as exc:
            raise RuntimeError("Failed to click the login button") from exc
        return self

    def login(self, username, password):
        self.enter_username(username)
        self.enter_password(password)
        self.click_login()
        return self

    def is_username_displayed(self, timeout=None):
        return self._is_displayed(self.USERNAME_INPUT, timeout)

    def is_password_displayed(self, timeout=None):
        return self._is_displayed(self.PASSWORD_INPUT, timeout)

    def is_login_button_displayed(self, timeout=None):
        return self._is_displayed(self.LOGIN_BUTTON, timeout)

    def get_error_message(self, timeout=None):
        try:
            return self._visible(self.AUTH_ERROR, timeout).text.strip()
        except RuntimeError as exc:
            raise RuntimeError("The authentication error message did not appear") from exc

    def is_error_displayed(self, timeout=None):
        return self._is_displayed(self.AUTH_ERROR, timeout)

    def get_field_validation_messages(self, timeout=None):
        try:
            elements = self._wait(timeout).until(
                EC.presence_of_all_elements_located(self.FIELD_ERROR)
            )
        except TimeoutException as exc:
            raise RuntimeError("The field validation messages did not appear") from exc
        return [element.text.strip() for element in elements]

    def get_dashboard_module_text(self, timeout=None):
        try:
            return self._visible(self.DASHBOARD_BREADCRUMB, timeout).text.strip()
        except RuntimeError as exc:
            raise RuntimeError("The dashboard module breadcrumb did not appear") from exc

    def is_authenticated(self, timeout=None):
        try:
            self._wait(timeout).until(EC.url_contains("dashboard/index"))
        except TimeoutException:
            return False
        return self._is_displayed(self.DASHBOARD_BREADCRUMB, timeout)

    def is_on_login_page(self, timeout=None):
        return self._is_displayed(self.USERNAME_INPUT, timeout)

    def get_current_url(self):
        return self.driver.current_url
