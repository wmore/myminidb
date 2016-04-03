package util;

/**
 * Created by wangyue on 16/3/28.
 */
public class LogToll {

    public static void printLog(String functionName, String content){
        System.out.println("==========="+System.currentTimeMillis()+functionName+content+": ===========");
    }

}
