package selenium.framework.report;

import org.testng.ITestNGMethod;

import java.util.Comparator;

/**
 * 测试方法比较器
 */
public class TestMethodComparator implements Comparator<ITestNGMethod> {
    public int compare(ITestNGMethod method1, ITestNGMethod method2) {
        int compare = method1.getRealClass().getName()
                .compareTo(method2.getRealClass().getName());
        if (compare == 0) {
            compare = method1.getMethodName()
                    .compareTo(method2.getMethodName());
        }
        return compare;
    }
}
