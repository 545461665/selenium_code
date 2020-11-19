package selenium.framework.report;

import org.testng.IClass;

import java.util.Comparator;

/**
 * 测试类比较器
 */
public class TestClassComparator implements Comparator<IClass> {
    public int compare(IClass class1, IClass class2) {
        return class1.getName().compareTo(class2.getName());
    }
}