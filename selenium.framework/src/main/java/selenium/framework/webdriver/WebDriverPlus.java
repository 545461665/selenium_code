package selenium.framework.webdriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WebDriver扩展类
 */
public class WebDriverPlus extends EventFiringWebDriver{

    //截图路径
    public static final String PFILEPATH="/test-output/screenshot";
    public WebDriverPlus(WebDriver driver) {
        super(driver);
    }

    /**
     * 自定义名字截取屏幕图片
     * @param name 图片名称
     */
    public void screenShot(String name){
        //当前项目名+/test-output/screenshot
        String path=System.getProperty("user.dir")+PFILEPATH;
        File file=new File(path);

        //如果文件不存在，创建
        if(!file.exists()){
            file.mkdir();
        }
        //指定了OutputType.FILE做为参数传递给getScreenshotAs()方法，其含义是将截取的屏幕以文件形式返回。
        File screenShotFile=((TakesScreenshot)DriverBase.driver).getScreenshotAs(OutputType.FILE);
        try {
            //利用FileUtils工具类的copyFile()方法保存getScreenshotAs()返回的文件对象。
            FileUtils.copyFile(screenShotFile,new File(path +"/"+ name + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *根据日期格式截图
     */
    public void screenShot(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        screenShot(df.format(new Date()).toString());

    }
}
