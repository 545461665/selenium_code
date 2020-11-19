package selenium.framework.dataprovider;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv文件提供类
 *
 * test_login.csv
 *   1	    2	    3
 eee	rrrr	rrr

 */
public class CsvDataProvider extends DataproviderImpl {
    //注解使用#
    public final String ANOTATION="#";
    //分离数据的符号
    public final char SEPARATE='|';

    /**
     *
     * @param caseName 方法名称
     * @param dataFile 测试数据文件
     */
    @Override
    protected void generateData(String caseName, String dataFile) {
        List<String[]> csvList=getCSVList(caseName,dataFile);
        if(csvList.equals(null)||csvList.size()==0){
            return;
        }
        for (int i = 1; i <=csvList.size() ; i++) {
            if(i>=start&&i<=max){
                String[]line=csvList.get(i-1);
                data.add(line);
            }
        }
    }

    /**
     * 获取CSV list列表
     * @param caseName 方法名称
     * @param dataFile 测试数据文件名
     * @return
     */
    private List<String[]> getCSVList(String caseName, String dataFile) {
        List<String[]>csvList=new ArrayList<String[]>();
        try {
            CSVReader reader=new CSVReader(new InputStreamReader(new FileInputStream(defaultPath+dataFile+ File.separator+caseName),
                    "UTF-8"),SEPARATE);
            String[]line=null;
            while ((line=reader.readNext())!=null){
                //判断第一行不为空或者以#号开始
                if(line.length==1&&line[0].isEmpty()||line[0].startsWith(ANOTATION)){
                    continue;
                }
                csvList.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return csvList;
    }
}
