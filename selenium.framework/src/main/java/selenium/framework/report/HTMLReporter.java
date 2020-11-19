package selenium.framework.report;

import org.apache.velocity.VelocityContext;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HTML 格式报告
 */
public class HTMLReporter extends AbstractReporter{
    //框架配置
    private static final String FRAMES_PROPERTY = "Report.frames";
    //报告失败配置
    private static final String ONLY_FAILURES_PROPERTY = "Report.failures-only";
    //模板路径配置
    private static final String TEMPLATES_PATH = "selenium/framework/report/templates/html/";
    //索引文件
    private static final String INDEX_FILE = "indexMain.html";
    //套件文件
    private static final String SUITES_FILE = "suites.html";
    //概述文件
    private static final String OVERVIEW_FILE = "overview.html";
    //组文件
    private static final String GROUPS_FILE = "groups.html";
    //结果文件
    private static final String RESULTS_FILE = "results.html";
    //顺序（年表）文件
    private static final String CHRONOLOGY_FILE = "chronology.html";
    //输出文件
    private static final String OUTPUT_FILE = "output.html";

    private static final String CUSTOM_STYLE_FILE = "custom.css";

    private static final String SUITE_KEY = "suite";
    private static final String SUITES_KEY = "suites";
    private static final String GROUPS_KEY = "groups";
    private static final String RESULT_KEY = "result";
    //失败的配置
    private static final String FAILED_CONFIG_KEY = "failedConfigurations";
    private static final String SKIPPED_CONFIG_KEY = "skippedConfigurations";
    private static final String FAILED_TESTS_KEY = "failedTests";
    private static final String SKIPPED_TESTS_KEY = "skippedTests";
    private static final String PASSED_TESTS_KEY = "passedTests";
    private static final String METHODS_KEY = "methods";
    private static final String ONLY_FAILURES_KEY = "onlyReportFailures";
    private static final String REPORT_DIRECTORY = "";
    private static final Comparator<ITestNGMethod> METHOD_COMPARATOR = new TestMethodComparator();
    private static final Comparator<ITestResult> RESULT_COMPARATOR = new TestResultComparator();
    private static final Comparator<IClass> CLASS_COMPARATOR = new TestClassComparator();

    public HTMLReporter() {
        super(TEMPLATES_PATH);
    }
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectoryName) {
        removeEmptyDirectories(new File(outputDirectoryName));
        outputDirectoryName = addTimeStampToPath(outputDirectoryName);
        boolean useFrames = System.getProperty(FRAMES_PROPERTY, "true").equals(
                "true");
        boolean onlyFailures = System.getProperty(ONLY_FAILURES_PROPERTY,
                "false").equals("true");
        File outputDirectory = new File(outputDirectoryName, REPORT_DIRECTORY);
        outputDirectory.mkdirs();
        try {
            if (useFrames) {
                createFrameset(outputDirectory);
            }
            createOverview(suites, outputDirectory, !useFrames, onlyFailures);
            createSuiteList(suites, outputDirectory, onlyFailures);
            createGroups(suites, outputDirectory);
            createResults(suites, outputDirectory, onlyFailures);
            // createChronology(suites, outputDirectory);
            createLog(outputDirectory, onlyFailures);
            copyResources(outputDirectory);
        } catch (Exception ex) {
            throw new ReportNGException("Failed generating HTML report.", ex);
        }
    }
    @SuppressWarnings("unused")
    private void createChronology(List<ISuite> suites, File outputDirectory)
            throws Exception {
        int index = 1;
        for (ISuite suite : suites) {
            List<IInvokedMethod> methods = suite.getAllInvokedMethods();
            if(!methods.isEmpty()) {
                VelocityContext context = createContext();
                context.put(SUITE_KEY, suite);
                context.put(METHODS_KEY, methods);
                String fileName = String.format("suite%d_%s", index,
                        CHRONOLOGY_FILE);
                generateFile(new File(outputDirectory, fileName),
                        CHRONOLOGY_FILE + TEMPLATE_EXTENSION, context);
            }
            ++index;
        }
    }
    private void copyResources(File outputDirectory) throws IOException {
        copyClasspathResource(outputDirectory, "reportng.css", "reportng.css");
        copyClasspathResource(outputDirectory, "reportng.js", "reportng.js");
        copyClasspathResource(outputDirectory, "sorttable.js", "sorttable.js");
        copyImgFile(outputDirectory, "logo.png", "logo.png");
        // 如果有一个自定义的样式表，则复制它。
        File customStylesheet = META.getStylesheetPath();

        if (customStylesheet != null) {
            if (customStylesheet.exists()) {
                copyFile(outputDirectory, customStylesheet, CUSTOM_STYLE_FILE);
            } else {
                // 如果找不到，可以尝试读取一个文件资源
                InputStream stream = ClassLoader.getSystemClassLoader()
                        .getResourceAsStream(customStylesheet.getPath());
                if (stream != null) {
                    copyStream(outputDirectory, stream, CUSTOM_STYLE_FILE);
                }
            }
        }
    }

    private void createLog(File outputDirectory, boolean onlyFailures)
            throws Exception {
        if (!Reporter.getOutput().isEmpty()) {
            VelocityContext context = createContext();
            context.put(ONLY_FAILURES_KEY, onlyFailures);
            generateFile(new File(outputDirectory, OUTPUT_FILE), OUTPUT_FILE
                    + TEMPLATE_EXTENSION, context);
        }
    }

    /**
     * 创建结果集
     * @param suites
     * @param outputDirectory
     * @param onlyShowFailures
     * @throws Exception
     */
    private void createResults(List<ISuite> suites, File outputDirectory,
                               boolean onlyShowFailures) throws Exception {
        int index = 1;
        for (ISuite suite : suites) {
            int index2 = 1;
            for (ISuiteResult result : suite.getResults().values()) {
                boolean failuresExist = result.getTestContext()
                        .getFailedTests().size() > 0
                        || result.getTestContext().getFailedConfigurations()
                        .size() > 0;
                if (!onlyShowFailures || failuresExist) {
                    VelocityContext context = createContext();
                    context.put(RESULT_KEY, result);
                    context.put(FAILED_CONFIG_KEY, sortByTestClass(result
                            .getTestContext().getFailedConfigurations()));
                    context.put(SKIPPED_CONFIG_KEY, sortByTestClass(result
                            .getTestContext().getSkippedConfigurations()));
                    context.put(FAILED_TESTS_KEY, sortByTestClass(result
                            .getTestContext().getFailedTests()));
                    context.put(SKIPPED_TESTS_KEY, sortByTestClass(result
                            .getTestContext().getSkippedTests()));
                    context.put(PASSED_TESTS_KEY, sortByTestClass(result
                            .getTestContext().getPassedTests()));
                    String fileName = String.format("suite%d_test%d_%s", index,
                            index2, RESULTS_FILE);
                    generateFile(new File(outputDirectory, fileName),
                            RESULTS_FILE + TEMPLATE_EXTENSION, context);
                }
                ++index2;
            }
            ++index;
        }
    }
    private SortedMap<IClass, List<ITestResult>> sortByTestClass(
            IResultMap results) {
        SortedMap<IClass, List<ITestResult>> sortedResults = new TreeMap<IClass, List<ITestResult>>(
                CLASS_COMPARATOR);
        for (ITestResult result : results.getAllResults()) {
            List<ITestResult> resultsForClass = sortedResults.get(result
                    .getTestClass());
            if (resultsForClass == null) {
                resultsForClass = new ArrayList<ITestResult>();
                sortedResults.put(result.getTestClass(), resultsForClass);
            }
            int index = Collections.binarySearch(resultsForClass, result,
                    RESULT_COMPARATOR);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
            resultsForClass.add(index, result);
        }
        return sortedResults;
    }
    /**
     * 创建组
     * @param suites
     * @param outputDirectory
     * @throws Exception
     */
    private void createGroups(List<ISuite> suites, File outputDirectory)
            throws Exception {
        int index = 1;
        for (ISuite suite : suites) {
            SortedMap<String, SortedSet<ITestNGMethod>> groups = sortGroups(suite
                    .getMethodsByGroups());
            if (!groups.isEmpty()) {
                VelocityContext context = createContext();
                context.put(SUITE_KEY, suite);
                context.put(GROUPS_KEY, groups);
                String fileName = String.format("suite%d_%s", index,
                        GROUPS_FILE);
                generateFile(new File(outputDirectory, fileName), GROUPS_FILE
                        + TEMPLATE_EXTENSION, context);
            }
            ++index;
        }
    }

    private SortedMap<String, SortedSet<ITestNGMethod>> sortGroups(
            Map<String, Collection<ITestNGMethod>> groups) {
        SortedMap<String, SortedSet<ITestNGMethod>> sortedGroups = new TreeMap<String, SortedSet<ITestNGMethod>>();
        for (Map.Entry<String, Collection<ITestNGMethod>> entry : groups
                .entrySet()) {
            SortedSet<ITestNGMethod> methods = new TreeSet<ITestNGMethod>(
                    METHOD_COMPARATOR);
            methods.addAll(entry.getValue());
            sortedGroups.put(entry.getKey(), methods);
        }
        return sortedGroups;
    }
    /**
     * 创建套件集合
     * @param suites
     * @param outputDirectory
     * @param onlyFailures
     * @throws Exception
     */
    private void createSuiteList(List<ISuite> suites, File outputDirectory,
                                 boolean onlyFailures) throws Exception {
        VelocityContext context = createContext();
        context.put(SUITES_KEY, suites);
        context.put(ONLY_FAILURES_KEY, onlyFailures);
        generateFile(new File(outputDirectory, SUITES_FILE), SUITES_FILE
                + TEMPLATE_EXTENSION, context);
    }

    /**
     *创建概述
     * @param suites
     * @param outputDirectory
     * @param isIndex
     * @param onlyFailures
     * @throws Exception
     */
    private void createOverview(List<ISuite> suites, File outputDirectory,
                                boolean isIndex, boolean onlyFailures) throws Exception {
        VelocityContext context = createContext();
        context.put(SUITES_KEY, suites);
        context.put(ONLY_FAILURES_KEY, onlyFailures);
        generateFile(new File(outputDirectory, isIndex ? INDEX_FILE
                : OVERVIEW_FILE), OVERVIEW_FILE + TEMPLATE_EXTENSION, context);
    }
    /**
     *创建框架集
     * @param outputDirectory
     * @throws Exception
     */
    private void createFrameset(File outputDirectory) throws Exception {
        VelocityContext context = createContext();
        generateFile(new File(outputDirectory, INDEX_FILE), INDEX_FILE
                + TEMPLATE_EXTENSION, context);
    }

    /**
     * 添加时间戳路径
     * @param path
     * @return
     */
    private String addTimeStampToPath(String path) {
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
        String dt = DATE_FORMAT.format(new Date());
        path = path + File.separator + System.getProperty("TestTimeStamp", dt);
        return path;
    }
}
