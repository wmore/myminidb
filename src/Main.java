import db.bean.DBEngine;
import db.filecontrol.MTableFileControl;
import net.sf.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        writeTest();
//        readTest();
        DBEngine dbEngine = new DBEngine();
        dbEngine.writeTable("lalal");
    }

    public static  void writeTest(){
        MTableFileControl mTableFileControl = new MTableFileControl();
        StringBuilder sb = new StringBuilder();
        int maxindex = 5;
        for (int i=0;i<maxindex;i++){
            sb.append((i+2000)+"asdf$_$"+(i+2000)+"o1i23o$_$o1i2o3i1"+(i+2000)+"o2i3$_$oi"+(i+2000));
            if (i!=maxindex-1) sb.append(";");
        }
        mTableFileControl.writeToFile("testjson.txt",sb.toString());
    }

    public static void readTest(){
        MTableFileControl mTableFileControl = new MTableFileControl();
        String jsonString = mTableFileControl.readFile("testjson.txt");
        System.out.println(jsonString);
        int length = jsonString.split(";").length;

        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();
        for (int i=0;i<length;i++){
            ArrayList<String> attributeRow = new ArrayList<String>();
            String subContent =jsonString.split(";")[i];
//            System.out.println(subContent);
            int length2 = subContent.split("\\$_\\$").length;
            for (int j =0;j<length2;j++) {
                attributeRow.add(subContent.split("\\$_\\$")[j]);
                System.out.print(subContent.split("\\$_\\$")[j] + "\t");
            }
            System.out.print("\n");
            attributeTuple.add(attributeRow);
        }

        System.out.println(attributeTuple);
        System.out.println(attributeTuple.get(0).get(1));
    }

}
