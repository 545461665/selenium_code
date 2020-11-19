package selenium.framework.webdriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.framework.util.Base;
import selenium.framework.util.Logger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 驱动基础类
 */
public class DriverBase extends Base {

    //截图后存放的目录，固定不变
    public static final String PFILEPATH = "/test-output/screenshot";

    private static WebDriver dr = null;
    public static WebDriverPlus driver = null;

    //浏览器名称
    public static String browser = null;

    //设置的等待时间 是针对全局设置
    public static int implicitlyWait = 30; //隐式等待时间：秒，针对Driver 每次执行命令的 最长执行时间也可以理解为超时时间
    public static int webDriverWait = 30;//显示等待时间：秒，要等到某个元素的出现，等不到就一直等直到超时

    /**
     * 启动浏览器
     */
    public static void launch() {
        //获取配置文件中的WebDriver.Browser的值
        browser = System.getProperty("WebDriver.Browser");
        //获取配置文件中的WebDriver.Browser.Location的值
        String browserLocation = System.getProperty("WebDriver.Browser.Location");

        /*Set<String> strings = InitProperties.mapproperties.keySet();
        for (String string : strings) {
            System.out.println("=================="+string+"==================");
        }*/

        //忽略大小写判断是否相等
        if (browser.equalsIgnoreCase("Firefox")) {
            Logger.log("打开Firefox浏览器");

            if (browserLocation != null && !browserLocation.isEmpty()) {
                //System.setProperty("webdriver.firefox.bin", browserLocation);
                System.setProperty("webdriver.gecko.driver", browserLocation);
            }
            dr = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("Chrome")) {
            Logger.log("打开Chrome浏览器");
            if (browserLocation != null && !browserLocation.isEmpty()) {
                System.setProperty("webdriver.chrome.driver", browserLocation);
            }
            dr = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Edge")) {
            Logger.log("打开Edge浏览器");
            if (browserLocation != null && !browserLocation.isEmpty()) {
                System.setProperty("webdriver.Edge.driver", browserLocation);
            }
            dr = new InternetExplorerDriver();
        } else {
            Logger.log("打开IE浏览器");
            if (browserLocation != null && !browserLocation.isEmpty()) {
                System.setProperty("webdriver.ie.driver", browserLocation);
            }
            dr = new EdgeDriver();
        }

        //扩展浏览器的操作方法类
        driver = new WebDriverPlus(dr);
        //获取配置文件WebDriver.ImplicitlyWait的值并赋值给implicitlyWait
        implicitlyWait = Integer.parseInt(System.getProperty("WebDriver.ImplicitlyWait").trim());
        //获取配置文件WebDriver.WebDriverWait的值并赋值给webDriverWait
        webDriverWait = Integer.parseInt(System.getProperty("WebDriver.WebDriverWait").trim());

        Logger.log("设置全局显示等待:" + implicitlyWait);
        //设置隐式等待超时时间
        driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
        //窗口最大化
        driver.manage().window().maximize();
    }

    /**
     * 输入值并验证输入正确
     *
     * @param element
     * @param text
     */
    public static void input(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
        valueToBe(element, text);
    }

    /**
     * 验证元素value与期望value是否相同，直到超时
     *
     * @param element 元素
     * @param value   期望值 value
     */
    public static void valueToBe(final WebElement element, final String value) {
        lightElement(element);
        //显示等待
        new WebDriverWait(driver, DriverBase.webDriverWait).until(
                new Function<WebDriver, Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        return element.getAttribute("value").equals(value);
                    }
                }
        );
        obumbrateElement(element);
    }

    /**
     * 通过JS注入输入值并验证输入正确，当给日期等空间输入值时，推荐使用此方法
     *
     * @param element 元素
     * @param text    输入值
     */
    public static void inputByJs(WebElement element, String text) {
        jsExecutor(element, String.format("arguments[0].value=\"%s\"", text));
        valueToBe(element, text);
    }

    /**
     * 选择下拉列表值但不验证是否选择正确
     *
     * @param element
     * @param text
     */
    public static void selectWithoutCheck(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    /**
     * 将鼠标焦点移至本元素
     *
     * @param element
     */
    public static void moveToElement(WebElement element) {
        lightElement(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        obumbrateElement(element);
    }

    /**
     * 使用action单击元素
     *
     * @param element
     */
    public static void click(WebElement element) {
        lightElement(element);
        Actions actions = new Actions(driver);
        actions.click(element).perform();
        obumbrateElement(element);
    }

    /**
     * 双击当前元素
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }

    /**
     * 在当前元素右击
     *
     * @param element
     */
    public static void rightClick(WebElement element) {
        Actions actions=new Actions(driver);
        actions.contextClick(element).perform();
    }

    /**
     * 拖曳当前页面元素至指定元素位置
      * @param source 起始位置元素
     * @param target 指定位置元素
     */
    public static void dragAndDropTo(WebElement source,WebElement target){
        (new Actions(driver)).dragAndDrop(source,target).perform();
    }

    /**
     * 获取元素对齐方式
     * @param element
     * @return left or right
     */
    public static String getTextAlin(WebElement element){
        return element.getCssValue("text-align");
}

    /**
     * 判断当前元素是否存在直至超时
     * @param by 定位方式：id，name
     * @param timeout 设置寻找元素超时时间
     * @return true or false
     */
    public static boolean isElementPresent(By by,int timeout){
        try {
            driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
            driver.findElement(by);
            driver.manage().timeouts().implicitlyWait(implicitlyWait,TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            driver.manage().timeouts().implicitlyWait(implicitlyWait,TimeUnit.SECONDS);
            return false;
        }
}

    /**
     * 判断当前元素是否存在
     * @param by
     * @return
     */
    public static boolean isElementPresent(By by){
        try {
            driver.findElement(by);
            driver.manage().timeouts().implicitlyWait(implicitlyWait,TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            driver.manage().timeouts().implicitlyWait(implicitlyWait,TimeUnit.SECONDS);
            return false;
        }
    }

    /**
     * 验证元素加载完成，直到超时
     * @param by
     */
    public static void toBePresent(final By by){
        new WebDriverWait(driver,DriverBase.webDriverWait).until(
                new Function<WebDriver, Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        return isElementPresent(by,implicitlyWait);
                    }
                }
        );
    }

    /**
     * 验证元素不加载，知道超时
     * @param by
     */
    public static void toBeNotPresent(final By by){
        new WebDriverWait(driver,DriverBase.webDriverWait).until(
                new Function<WebDriver, Boolean>() {
                    @Override
                    public Boolean apply(WebDriver driver) {
                        return !isElementPresent(by,3);
                    }
                }
        );
    }

    /**
     * 验证元素可见，知道超时
     * @param element
     */
    public static void toBeVisible(WebElement element){
        lightElement(element);
        new WebDriverWait(driver,DriverBase.webDriverWait).until(ExpectedConditions.visibilityOf(element));
        obumbrateElement(element);
}

    /**
     * 验证元素不可见，直到超时
     * @param element
     */
    public static void toBeInvisible(final WebElement element){
        lightElement(element);
        new WebDriverWait(driver,DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !element.isDisplayed();
            }
        });
        obumbrateElement(element);
}

    /**
     * 验证元素可用，知道超时
     * @param element
     */
    public static void toBeEnable(WebElement element){
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(ExpectedConditions.elementToBeClickable(element));
        obumbrateElement(element);
}

    /**
     * 验证元素不可用，直到超时
     *
     * @param element
     */
    public static void toBeDisable(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return !element.isEnabled();
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素text与期望text相同，直到超时
     *
     * @param element
     * @param text    期望 text
     */
    public static void textToBe(final WebElement element, final String text) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.getText().equals(text);
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素text为空，直到超时
     *
     * @param element
     */
    public void textToBeEmpty(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.getText().isEmpty();
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素text不为空，直到超时
     *
     * @param element
     */
    public void textToBeNotEmpty(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return !element.getText().isEmpty();
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素text包含期望text，直到超时
     *
     * @param element
     * @param text    期望被包含的 text
     */
    public void textContains(final WebElement element, final String text) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.getText().contains(text);
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素value包含期望value，直到超时
     *
     * @param element
     * @param value   期望被包含的 value
     */
    public void valueContains(final WebElement element, final String value) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.getAttribute("value").contains(value);
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素value为空，直到超时
     *
     * @param element
     */
    public void valueToBeEmpty(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return element.getAttribute("value").isEmpty();
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证元素value不为空，直到超时
     *
     * @param element
     */
    public void valueToBeNotEmpty(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return !element.getAttribute("value").isEmpty();
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证单选框、复选框元素被选中，直到超时
     *
     * @param element
     */
    public void toBeSelected(WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(ExpectedConditions.elementToBeSelected(element));
        obumbrateElement(element);
    }


    /**
     * 验证单选框、复选框元素不被选中，直到超时
     *
     * @param element
     */
    public void toBeNotSelected(WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait)
                .until(ExpectedConditions.elementSelectionStateToBe(element, false));
        obumbrateElement(element);
    }

    /**
     * 验证当前元素获取焦点，直到超时
     *
     * @param element
     */
    public void toBeActive(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.switchTo().activeElement().getLocation().equals(element.getLocation());
            }
        });
        obumbrateElement(element);
    }

    /**
     * 验证当前元素没有获取焦点，直到超时
     *
     * @param element
     */
    public void toBeNotActive(final WebElement element) {
        lightElement(element);
        new WebDriverWait(driver, DriverBase.webDriverWait).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return !driver.switchTo().activeElement().getLocation().equals(element.getLocation());
            }
        });
        obumbrateElement(element);
    }

    /**
     * 通过元素标签和标签内内容返回一个新元素, 新元素通过xpath定位，xpath值为：//tag[contains(text(),'name')]
     *
     * @param tag  标签值
     * @param name name值
     * @return Element
     */
    public WebElement getElementByTagAndName(String tag, String name) {
        return driver.findElement(By.xpath("//" + tag + "[contains(text(),'" + name + "')]"));
    }


    /**
     * 拖曳滚动条使当前元素至视野内
     *
     * @param element
     */
    public static void scrollIntoView(WebElement element) {
        //arguments[0].scrollIntoView();对滚动条进行操作，scrollIntoView：拉到最后
        jsExecutor(element, "arguments[0].scrollIntoView();");
    }

    /**
     * JS注入内容
     *
     * @param element
     * @param text    需要注入的内容
     */
    public static void jsDate(WebElement element, String text) {
        //arguments[0].value="%s":获取当前元素的值
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=\"%s\"", text, element);
    }

    /**
     * 去掉当前元素高亮效果
     *
     * @param element 元素
     */
    public static void obumbrateElement(WebElement element) {
        try {
            Thread.sleep(100);
            jsExecutor(element, "arguments[0].style.border = 'none'");
        } catch (NoSuchElementException e) {
            return;
        } catch (Exception e) {
        }
    }

    /**
     * 高亮当前元素
     *
     * @param element
     */
    public static void lightElement(WebElement element) {
        try {
            //arguments[0].style.border = '4px solid red':对元素的属性进行注入
            jsExecutor(element, "arguments[0].style.border = '4px solid red'");
        } catch (NoSuchElementException e) {
            return;
        }
    }

    /**
     * 执行javascript
     *
     * @param element
     * @param script
     */
    public static void jsExecutor(WebElement element, String script) {
        try {
            ((JavascriptExecutor) driver).executeScript(script, element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 浏览器多窗口切换
     *
     * @param driver        浏览器驱动
     * @param currentWindow 当前浏览器窗口
     * @param title         页面title
     */
    public static void swithWin(WebDriver driver, String currentWindow, String title) {

        //得到所有窗口的句柄
        Set<String> handles = driver.getWindowHandles();

        for (String handle : handles) {
            //判断是否是之前定义的handle
            if (!handle.equals(currentWindow)) {
                driver.switchTo().window(handle);//切换到新的窗口
                delay(2);
                if (driver.getTitle().contains(title)) {
                    // System.out.println("我是当前窗口：" + handle + "：" + driver.getTitle());
                    break;
                }
            }
        }
    }


    /**
     * 模拟粘贴文本
     *
     * @param text 文本内容
     */
    public void paste(String text) {
        //把text写到剪贴板
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(text), null);

        //模拟键盘的CTRL+V操作
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);//按下键盘Ctrl键
            robot.keyPress(KeyEvent.VK_V);//按下键盘V键

            robot.keyRelease(KeyEvent.VK_CONTROL);//放开键盘Ctrl键
            robot.keyRelease(KeyEvent.VK_V);//放开键盘V键
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
