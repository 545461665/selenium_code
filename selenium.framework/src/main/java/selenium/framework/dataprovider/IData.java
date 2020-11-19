package selenium.framework.dataprovider;

/**
 * 文件数据提供程序接口
 */
public interface IData {
    public Object[][] getData(String caseName,String dataFile);
    public Object[][] getData(String caseName,String dataFile,int startRowNum);
    public Object[][] getData(String caseName,String dataFile,int beginNum,int endNum);
}
