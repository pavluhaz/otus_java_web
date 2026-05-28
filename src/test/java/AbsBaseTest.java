import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.lang.reflect.Method;

public abstract class AbsBaseTest {

    protected WebDriver driver;

    private final String DEFAULT_BASE_URL = System.getProperty("base.url", "https://otus.home.kartushin.su");
    private static final String TRAINING_PAGE_PATH = "/training.html";

    @BeforeAll
    static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        BrowserMode browserMode = getBrowserMode(testInfo);

        driver = new ChromeDriver(getChromeOptions(browserMode));
        setupDriver(browserMode);
        openTrainingPage();
    }

    private void setupDriver(BrowserMode browserMode) {
        if (browserMode == BrowserMode.MAXIMIZED) {
            driver.manage().window().maximize();
        }
    }

    private void openTrainingPage() {
        driver.get(DEFAULT_BASE_URL + TRAINING_PAGE_PATH);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private ChromeOptions getChromeOptions(BrowserMode browserMode) {
        ChromeOptions options = new ChromeOptions();

        switch (browserMode) {
            case HEADLESS:
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                break;
            case KIOSK:
                options.addArguments("--kiosk");
                break;
            case MAXIMIZED:
                options.addArguments("--start-maximized");
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser mode: " + browserMode);
        }

        return options;
    }

    private BrowserMode getBrowserMode(TestInfo testInfo) {
        return testInfo.getTestMethod()
                .map(this::getBrowserModeFromAnnotation)
                .orElse(BrowserMode.MAXIMIZED);
    }

    private BrowserMode getBrowserModeFromAnnotation(Method method) {
        BrowserConfig browserConfig = method.getAnnotation(BrowserConfig.class);

        if (browserConfig == null) {
            return BrowserMode.MAXIMIZED;
        }

        return browserConfig.mode();
    }
}
