package selenium.framework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BaiduHomePage {
    /**
     * 百度首页登录按钮
     */
    @FindBy(xpath = "//div[@id='u1']/a")
    public WebElement login_button;

    /**
     * 用户名登录超链接
     */
    @FindBy(id = "TANGRAM__PSP_11__footerULoginBtn")
    public WebElement user_link;

    /**
     * 用户名输入框
     */
    @FindBy(id = "TANGRAM__PSP_11__userName")
    public WebElement username_input;

    /**
     * 密码输入框
     */
    @FindBy(id = "TANGRAM__PSP_11__password")
    public WebElement password_input;


    /**
     * 用户登录按钮
     */
    @FindBy(id = "TANGRAM__PSP_11__submit")
    public WebElement login_submit;

    /**
     * 错误提示信息
     */
    @FindBy(id = "TANGRAM__PSP_11__error")
    public  WebElement error_lab;

    /**
     * 关闭登录弹出框
     */
    @FindBy(id = "TANGRAM__PSP_4__closeBtn")
    public WebElement close_button;

}
