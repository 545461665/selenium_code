package selenium.framework.report;

/**
 * 测试报告错误类
 */
@SuppressWarnings("serial")
public class ReportNGException extends RuntimeException {
    public ReportNGException(String string) {
        super(string);
    }

    public ReportNGException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
