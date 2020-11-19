package selenium.framework.listener;

import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

/**
 * 主监听测试框架
 * 
 *
 */
public class SeleniumListener implements IExecutionListener, ISuiteListener, IInvokedMethodListener{
	  
	//测试开始监听
	@Override
	public void onExecutionStart() {
		//测试开始监听
		System.out.println("=======================================================");
		System.out.println("                    测试框架执行开始");
		System.out.println("=======================================================");
	}

	//测试结束监听
	@Override
	public void onExecutionFinish() {
		System.out.println("=======================================================");
		System.out.println("                    测试监听结束");
		System.out.println("=======================================================");
	}

	@Override
	public void onStart(ISuite suite) {
		System.out.println("测试套件【" + suite.getName() + "】执行开始");
	}

	@Override
	public void onFinish(ISuite suite) {
		System.out.println("测试套件【" + suite.getName() + "】执行结束");
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		System.out.println("测试方法【" + method.getTestMethod().getMethodName() + "】执行开始");
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}
	
}
