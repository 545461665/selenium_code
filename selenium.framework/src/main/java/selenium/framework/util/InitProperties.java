package selenium.framework.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 初始化配置类
 */
public class InitProperties {
    //配置文件的路径
    public static final String PFILEPATH = File.separatorChar + "config" + File.separatorChar + "CONFIG.properties";
    private InputStreamReader fn = null;
    private InputStream in = null;
    private Properties config = new Properties();
    public static Map<String, String> mapproperties = new HashMap<String, String>();

    public InitProperties() {
        //构造初始配置文件
        init();
    }

    /**
     * 初始化Property配置文件，放入系统属性变量中
     */
    private void init() {
        Logger.Defaultlog("初始化配置文件");
        try {
            //调用 对象的getClass()方法是获得对象当前的类类型
            in = getClass().getClassLoader().getResourceAsStream(PFILEPATH);
            config.load(new InputStreamReader(in, "utf-8"));

            System.out.println(config.getProperty("WebDriver.Browser.Location"));

            if (!config.isEmpty()) {
                Set<Object> keys = config.keySet();
                for (Object key : keys) {
                    InitProperties.mapproperties.put(key.toString(), config.getProperty(key.toString()));
                    //系统参数不包含指定的键名且配置文件的键值不为空
                    if (!System.getProperties().containsKey(key.toString()) && !config.getProperty(key.toString()).isEmpty()) {
                        //置指定键对值的系统属性
                        System.setProperty(key.toString(), config.getProperty(key.toString()));
                    }
                }
                keys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //fn.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // }

    /**
     * 对外调试使用
     */
    public static void showAllSystemProperties() {
        Set<String> syskeys = InitProperties.mapproperties.keySet();
        for (Object key : syskeys) {
            if (System.getProperties().containsKey(key)) {
                System.out.println(key.toString() + "  " + System.getProperty(key.toString()));
            }
        }
        syskeys.clear();
    }
}
