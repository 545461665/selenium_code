package selenium.framework.util;

import org.testng.annotations.DataProvider;
import selenium.framework.dataprovider.CsvDataProvider;
import selenium.framework.dataprovider.XmlDataProvider;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * 初始化基础类
 */
public class Base {
    //初始化InitProperties配置文件类
    static{
        new InitProperties();
        //Logger.setLog();
    }
    /**
     * 设置等待时间 单位为秒
     */
    public static void delay(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * xml数据驱动调用
     * 一个类就是一个xml文件
     * case的name值，必须是@Test方法名
     * @param m
     * @return
     */
    @DataProvider(name = "xml")
    protected Object[][]xmlData(Method m){
        //getSimpleName:获取类名+.xml文件
        return new XmlDataProvider().getData(m.getName(),
                m.getDeclaringClass().getSimpleName()+".xml");
    }

    /**
     * csv数据驱动调用
     * @param m
     * @return
     */
    @DataProvider(name="csv")
    protected  Object[][]csvData(Method m){
        return new CsvDataProvider().getData(m.getName()+".csv",
                m.getDeclaringClass().getSimpleName());
    }

    /**
     * @param type 所期望返回的随机数的类型，包括int，nint（负整数），double，ndouble（负double），char，uchar（特殊字符）, china（中文）
     * @param length 所期望返回的随机数的长度
     * @return 随机数
     * @throws Exception
     */
    public static String getRandomData(String type, int length) {
        String data = "";
        String negativeData = "";
        String charData[] = { "!", "@", "#", "$", "%", "^", "&", "*" };
        if (type.equals("int")) {
            for (int i = 0; i < length - 1; i++) {
                data += (int) (10 * Math.random());
            }
            data = (int) (9 * Math.random() + 1) + data;
        } else if (type.equals("nint")) {
            for (int i = 0; i < length - 1; i++) {
                data += (int) (10 * Math.random());
            }
            data = "-" + (int) (9 * Math.random() + 1) + data;
        } else if (type.equals("double")) {
            for (int i = 0; i < length - 3; i++) {
                data += (int) (10 * Math.random());
            }
            for (int i = 0; i < 2; i++) {
                negativeData += (int) (10 * Math.random());
            }
            data = (int) (9 * Math.random() + 1) + data + "." + negativeData;
        } else if (type.equals("ndouble")) {
            for (int i = 0; i < length - 3; i++) {
                data += (int) (10 * Math.random());
            }
            for (int i = 0; i < 2; i++) {
                negativeData += (int) (10 * Math.random());
            }
            data = "-" + (int) (9 * Math.random() + 1) + data + "."+ negativeData;
        } else if (type.equals("char")) {
            for (int i = 0; i < length; i++) {
                data += String.valueOf((char) ('a' + (int) (Math.random() * 26)));
            }
        } else if (type.equals("uchar")) {
            for (int i = 0; i < length; i++) {
                Random rnd = new Random();
                data += charData[rnd.nextInt(8)];
            }
        }else if (type.equals("china")) {
            for (int i = 0; i < length; i++) {
                data += "中";
            }
        }
        return data;
    }
}
