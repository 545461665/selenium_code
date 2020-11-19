package selenium.framework.report;


import org.testng.ITestResult;

import java.util.Comparator;

/**
 * 测试报告结果比较器
 */
public class TestResultComparator implements Comparator<ITestResult> {
    public int compare(ITestResult result1, ITestResult result2) {
        return result1.getName().compareTo(result2.getName());
    }
}
