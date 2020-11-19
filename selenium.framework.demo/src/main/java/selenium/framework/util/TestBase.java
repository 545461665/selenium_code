package selenium.framework.util;

import org.testng.annotations.*;
import selenium.framework.webdriver.DriverBase;

/**
 * 浏览器工具启动类
 */
public class TestBase extends DriverBase {
    @BeforeTest
    public void beforeMethodBase() {
        launch();
    }

    @AfterTest(alwaysRun=true)
    public void afterMethod() {
        quit();
    }

    //@BeforeClass
    public void beforeBaseClass() {
    }

    //@AfterClass(alwaysRun=true)
    public void afterBaseClass() {

    }

    /**
     * quit browser
     */
    private void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
