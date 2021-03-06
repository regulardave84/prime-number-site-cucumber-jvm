package support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class DriverSupport {
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final String DRIVER = "driver";
    private static final String OS_NAME = "os.name";
    private static final String WINDOWS = "Windows";
    private static final String MAC = "Mac";
    private static final String WEB_DRIVER_PATH = "web_drivers_path";
    private static final String MAC_DRIVER_PATH = "mac_driver_path";
    private static final String WINDOWS_DRIVER_PATH = "windows_driver_path";
    private static final String LINUX_DRIVER_PATH = "linux_driver_path";
    private static final String DEFAULT_DRIVER_PATH = LINUX_DRIVER_PATH;

    private static PropertiesSupport properties = new PropertiesSupport();
    private static String webDriver = properties.getValueFromFile(CONFIG_PROPERTIES, DRIVER);
    private static WebDriver driver;

    private DriverSupport() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            System.setProperty(webDriver, getDriverPath());
            driver = new ChromeDriver();
        }

        return driver;
    }

    private static String getDriverPath() {
        return getWebDriversPath() + getOSSpecificDriverPath();
    }

    private static String getOSSpecificDriverPath() {
        String path = "";

        if (System.getProperty(OS_NAME).startsWith(WINDOWS)) {
            path = WINDOWS_DRIVER_PATH;
        }
        else if (System.getProperty(OS_NAME).startsWith(MAC)) {
            path = MAC_DRIVER_PATH;
        }
        else {
            path = DEFAULT_DRIVER_PATH;
        }

        return properties.getValueFromFile(CONFIG_PROPERTIES, path);
    }

    private static String getWebDriversPath() {
        return properties.getValueFromFile(CONFIG_PROPERTIES, WEB_DRIVER_PATH);
    }

    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            driver.close();
        }
    };

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

}