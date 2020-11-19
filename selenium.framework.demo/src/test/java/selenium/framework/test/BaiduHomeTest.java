package selenium.framework.test;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import selenium.framework.pages.BaiduHomePage;
import selenium.framework.util.TestBase;
import selenium.framework.util.Logger;

public class BaiduHomeTest extends TestBase {
    BaiduHomePage baiduHome=null;

    @BeforeClass
    public void initPage(){
        baiduHome= PageFactory.initElements(driver,BaiduHomePage.class);
    }
    @BeforeMethod
    public void setUp(){
        Logger.log("打开百度首页");
        driver.get("https://www.baidu.com/");
        delay(2);
    }
    @Test(dataProvider = "xml")
    public void login_baidu(String username,String password,String message){
        Logger.log("点击登录链接，弹出登录框");
        baiduHome.login_button.click();
        delay(2);

        Logger.log("验证是否是用户名登录");
        boolean flag =baiduHome.user_link.isDisplayed();
        if(flag){
            Logger.log("点击【用户名登录】链接");
            baiduHome.user_link.click();
            delay(2);
        }

        Logger.log("输入用户名");
        baiduHome.username_input.clear();
        baiduHome.username_input.sendKeys(username);
        delay(2);

        Logger.log("输入密码");
        baiduHome.password_input.clear();
        baiduHome.password_input.sendKeys(password);
        delay(2);

        Logger.log("点击【登录】按钮");
       baiduHome.login_submit.click();
        delay(2);

        if(username.isEmpty()||password.isEmpty()){
            Logger.log("断言提示信息是否一致");
            Assert.assertEquals(message,baiduHome.error_lab.getText());
        }

    }


    //@DataProvider(name="LoginParam")
    public Object[][] dataValues(){
        return new Object[][]{
                {"111","","请您输入密码"},
                {"","222","请您输入手机/邮箱/用户名"},
                {"","","请您输入手机/邮箱/用户名"}
        };
    }
}
