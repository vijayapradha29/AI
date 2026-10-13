import os

DEFAULT_BASE_URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"

BASE_URL = os.getenv("ORANGEHRM_BASE_URL", DEFAULT_BASE_URL)
BROWSER = os.getenv("ORANGEHRM_BROWSER", "chrome").strip().lower()
HEADLESS = os.getenv("ORANGEHRM_HEADLESS", "false").strip().lower() in {"1", "true", "yes", "on"}
EXPLICIT_WAIT = float(os.getenv("ORANGEHRM_EXPLICIT_WAIT", "20"))
PAGE_LOAD_TIMEOUT = float(os.getenv("ORANGEHRM_PAGE_LOAD_TIMEOUT", "60"))
SCREENSHOT_DIR = os.getenv("ORANGEHRM_SCREENSHOT_DIR", os.path.join("reports", "screenshots"))

USERNAME_ENV = "ORANGEHRM_USERNAME"
PASSWORD_ENV = "ORANGEHRM_PASSWORD"

DASHBOARD_URL_PATH = "dashboard/index"


def get_credentials():
    return os.getenv(USERNAME_ENV), os.getenv(PASSWORD_ENV)


def credentials_available():
    username, password = get_credentials()
    return bool(username and password)
