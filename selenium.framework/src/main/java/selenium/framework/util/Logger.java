package selenium.framework.util;

import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志
 */
public class Logger {
    //日期格式
    private static final DateFormat DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //日志开关
    public static boolean isLog=true;
    //框架默认日志开关
    public static boolean isDefault=true;
    //控制台输出开关
    public static boolean isToStandardOut=true;
    //日志格式开关
    public static boolean isFormat=true;
    //命令行信息打印等级(1-5)
    public static int verbose=1;

    /**
     * 日志输出的相关信息
     * @param s
     * @param level
     * @param logToStandardOut
     */
    public static void log(String s,int level,boolean logToStandardOut){
        if(isLog){
            Reporter.log(logPrefix(s),level,logToStandardOut);
        }
    }

    public static void log(String s){
        log(s,verbose,isToStandardOut);
    }
    public static void log(String s,int level){
        log(s,verbose,isToStandardOut);
    }
    public static void log(String s,boolean logToStandardOut){
        log(s,verbose,logToStandardOut);
    }

    /**
     * 默认日志
     * @param s 日志输出信息
     */
    public static void Defaultlog(String s){
        if(isLog&&isDefault){
            Reporter.log(logPrefix(s),verbose,isToStandardOut);
        }
    }

    /**
     * 日志前缀
     * @param s
     * @return
     */
    private static String logPrefix(String s) {
        Date logtime = new Date();
        if(isFormat) {
            return "[" + System.getProperty("Project.Name", "baidu_test") + " "+ DATE_FORMAT.format(logtime) + "] " + s;
        }
        return s;
    }

    /**
     * 对日志开关的操作
     */
    public static void setLog(){
        //获得properties 配置文件Logger的值,如果是false
        if(System.getProperty("Logger","true").equalsIgnoreCase("false")){
            //关闭日志开关
            Logger.isLog=false;
        }
        //Logger.StandardOut的值，如果是true
        if (System.getProperty("Logger.StandardOut", "false").equalsIgnoreCase("true")) {
            //控制台输出开关打开
            Logger.isToStandardOut = true;
        }
        //如果Logger.FrameWorkOut的值是false
        if (System.getProperty("Logger.FrameWorkOut", "true").equalsIgnoreCase("false")) {
            //框架默认日志开关关闭
            Logger.isDefault = false;
        }
        //如果Logger.Format的值为false
        if (System.getProperty("Logger.Format", "true").equalsIgnoreCase("false")) {
            //日志格式开关关闭
            Logger.isFormat = false;
        }
    }


}
