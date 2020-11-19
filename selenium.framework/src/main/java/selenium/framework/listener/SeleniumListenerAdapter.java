package selenium.framework.listener;

//import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import selenium.framework.util.Logger;
import selenium.framework.webdriver.DriverBase;

//import com.longteng.qa.webdriver.DriverBase;


/**
 * 监听适配测试框架
 * 
 *
 */
public class SeleniumListenerAdapter extends TestListenerAdapter {
//	private static Logger logger = Logger.getLogger(SeleniumListenerAdapter.class);  
	
	//测试集开始监听
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		System.out.println(String.format("测试集【%s】执行开始 ", testContext.getName()));
	}

	//测试集结束监听
	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		System.out.println(String.format("测试集【%s】执行结束", testContext.getName()));
	}
	
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		System.out.println("=======================================================");
		System.out.println("测试方法 【"+tr.getName()+"】 执行失败");
		if(DriverBase.driver!=null){
			//截图
			String name = String.valueOf(System.currentTimeMillis());
			DriverBase.driver.screenShot(name);
			Logger.log("请参考：<a href=\"../screenshot/"+name+".jpg"+"\" style=\"color:red;\">"+name+".jpg"+"</font></a>");

		}
		System.out.println("失败原因为："+tr.getThrowable().getMessage());
		System.out.println("=======================================================");
	}

    @Override
    public void onTestSkipped(ITestResult tr) {
    	super.onTestSkipped(tr);
    	System.out.println("=======================================================");
    	System.out.println("测试方法 【"+tr.getName()+"】 执行跳过");
    	System.out.println("=======================================================");
    }
	 
    @Override
    public void onTestSuccess(ITestResult tr) {
    	super.onTestSuccess(tr);
    	System.out.println("=======================================================");
    	System.out.println("测试方法 【"+tr.getName()+"】 执行成功！");
    	System.out.println("=======================================================");
    }
   
}

