package selenium.framework.report;

import org.apache.velocity.VelocityContext;
import org.testng.IClass;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.*;

public class JUnitXMLReporter extends AbstractReporter {

    private static final String RESULTS_KEY = "results";
    private static final String TEMPLATES_PATH = "selenium/framework/report/templates/xml/";
    private static final String RESULTS_FILE = "results.xml";
    private static final String REPORT_DIRECTORY = "xml";

    public JUnitXMLReporter() {
        super(TEMPLATES_PATH);
    }
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectoryName) {
        removeEmptyDirectories(new File(outputDirectoryName));

        File outputDirectory = new File(outputDirectoryName, REPORT_DIRECTORY);
        outputDirectory.mkdir();

        Collection<TestClassResults> flattenedResults = flattenResults(suites);

        for (TestClassResults results : flattenedResults) {
            VelocityContext context = createContext();
            context.put(RESULTS_KEY, results);

            try {
                generateFile(new File(outputDirectory, results.getTestClass()
                        .getName() + '_' + RESULTS_FILE), RESULTS_FILE
                        + TEMPLATE_EXTENSION, context);
            } catch (Exception ex) {
                throw new ReportNGException(
                        "Failed generating JUnit XML report.", ex);
            }
        }
    }
    private Collection<TestClassResults> flattenResults(List<ISuite> suites) {
        Map<IClass, TestClassResults> flattenedResults = new HashMap<IClass, TestClassResults>();
        for (ISuite suite : suites) {
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                // Failed and skipped configuration methods are treated as test
                // failures.
                organiseByClass(suiteResult.getTestContext()
                                .getFailedConfigurations().getAllResults(),
                        flattenedResults);
                organiseByClass(suiteResult.getTestContext()
                                .getSkippedConfigurations().getAllResults(),
                        flattenedResults);
                // Successful configuration methods are not included.

                organiseByClass(suiteResult.getTestContext().getFailedTests()
                        .getAllResults(), flattenedResults);
                organiseByClass(suiteResult.getTestContext().getSkippedTests()
                        .getAllResults(), flattenedResults);
                organiseByClass(suiteResult.getTestContext().getPassedTests()
                        .getAllResults(), flattenedResults);
            }
        }
        return flattenedResults.values();
    }
    private void organiseByClass(Set<ITestResult> testResults,
                                 Map<IClass, TestClassResults> flattenedResults) {
        for (ITestResult testResult : testResults) {
            getResultsForClass(flattenedResults, testResult).addResult(
                    testResult);
        }
    }
    private TestClassResults getResultsForClass(
            Map<IClass, TestClassResults> flattenedResults,
            ITestResult testResult) {
        TestClassResults resultsForClass = flattenedResults.get(testResult
                .getTestClass());
        if (resultsForClass == null) {
            resultsForClass = new TestClassResults(testResult.getTestClass());
            flattenedResults.put(testResult.getTestClass(), resultsForClass);
        }
        return resultsForClass;
    }
    public static final class TestClassResults {
        private final IClass testClass;
        private final Collection<ITestResult> failedTests = new LinkedList<ITestResult>();
        private final Collection<ITestResult> skippedTests = new LinkedList<ITestResult>();
        private final Collection<ITestResult> passedTests = new LinkedList<ITestResult>();

        private long duration = 0;

        private TestClassResults(IClass testClass) {
            this.testClass = testClass;
        }

        public IClass getTestClass() {
            return testClass;
        }

        /**
         * Adds a test result for this class. Organises results by outcome.
         */
        void addResult(ITestResult result) {
            switch (result.getStatus()) {
                case ITestResult.SKIP: {
                    if (META.allowSkippedTestsInXML()) {
                        skippedTests.add(result);
                        break;
                    }
                    // Intentional fall-through (skipped tests marked as failed if
                    // XML doesn't support skips).
                }
                case ITestResult.FAILURE:
                case ITestResult.SUCCESS_PERCENTAGE_FAILURE: {
                    failedTests.add(result);
                    break;
                }
                case ITestResult.SUCCESS: {
                    passedTests.add(result);
                    break;
                }
            }
            duration += (result.getEndMillis() - result.getStartMillis());
        }

        public Collection<ITestResult> getFailedTests() {
            return failedTests;
        }

        public Collection<ITestResult> getSkippedTests() {
            return skippedTests;
        }

        public Collection<ITestResult> getPassedTests() {
            return passedTests;
        }

        public long getDuration() {
            return duration;
        }
    }
}
