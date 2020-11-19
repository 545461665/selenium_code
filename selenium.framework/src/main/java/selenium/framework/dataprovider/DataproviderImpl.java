package selenium.framework.dataprovider;

import java.util.ArrayList;
import java.util.List;

public class DataproviderImpl implements IData {

    //数据文件存放的路径
    public  String defaultPath="resources/testdata/data/";

    //起始的数据记录
    protected int start=1;

    //最大数据记录
    protected int max=Integer.MAX_VALUE;

    protected List<Object[]>data=new ArrayList<Object[]>();

    public static void main(String[] args) {

    }

    /**
     * 从文件获取数据
     * @param caseName 数据集名称
     * @param dataFile  文件数据
     * @return
     */
    @Override
    public Object[][] getData(String caseName, String dataFile) {
        generateData(caseName,dataFile);
        return generateArrayData();
    }

    /**
     * 从文件中获取数据
     * @param caseName 数据集名称
     * @param dataFile 数据文件
     * @param startRowNum 起始的数据记录，默认为1
     * @return
     */
    @Override
    public Object[][] getData(String caseName, String dataFile, int startRowNum) {
        start=startRowNum;
        return getData(caseName,dataFile);
    }

    /**
     * @param caseName 数据集名称
     * @param dataFile 数据文件
     * @param startRowNum 起始的数据记录，默认为1
     * @param length 获取数据的长度，默认为全部，包括起始数据
     * @return
     */
    @Override
    public Object[][] getData(String caseName, String dataFile, int startRowNum, int length) {
        max=startRowNum+length;
        return getData(caseName,dataFile,startRowNum);
    }

    /**
     * 具体的文件驱动需要重写这个方法
     * @param caseName
     * @param dataFile
     */
    protected void generateData(String caseName,String dataFile){}

    /**
     * 读取数据
     * @return
     */
    protected Object[][]generateArrayData(){
        int i=0;
        Object[][]o=new Object[data.size()][];
        for (Object[] oo : data) {
            o[i++]=oo;
        }
        data.clear();
        return o;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
}
