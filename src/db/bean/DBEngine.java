package db.bean;

/**
 * Created by wangyue on 16/3/25.
 */

import db.filecontrol.MTableFileControl;

import java.util.ArrayList;
import java.util.HashMap;


/**************
 * {
 * "name":"tableName",
 * "attritubeList":[
 * {
 * "attributeName": "name",
 * "attributeType": "type",
 * "isPrimaryKey": "true or false",
 * "primaryKeyValue": "value"
 * },
 * {
 * "attributeName": "name",
 * "attributeType": "type",
 * "length": "length ",
 * "isPrimaryKey": "true or false",
 * "primaryKeyValue": "value"
 * }
 * ],
 * "rows":[
 * [" "," ", " "],
 * [" "," ", " "]
 * ],
 * }
 ******************/
public class DBEngine {
    private HashMap<String, MTable> tablesMap = new HashMap<String, MTable>();
    private MTableFileControl mTableFileControl = new MTableFileControl();

    final String SUBS_TABLES = "SUBS_TABLES.txt";

    public DBEngine() {
    }

    public void createTable(String name, ArrayList<String> attribute_types, ArrayList<String> attribute_names) {
        MTable newTable = new MTable(name, attribute_types, attribute_names);
        tablesMap.put(name, newTable);
    }

    public ArrayList<ArrayList<String>> queryAttributeTuple(String name) {

        String jsonString = mTableFileControl.readFile("testjson.txt");
        System.out.println(jsonString);
        int length = jsonString.split(";").length;

        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < length; i++) {
            ArrayList<String> attributeRow = new ArrayList<String>();
            String subContent = jsonString.split(";")[i];
            int length2 = subContent.split("\\$_\\$").length;
            for (int j = 0; j < length2; j++) {
                attributeRow.add(subContent.split("\\$_\\$")[j]);
            }
            attributeTuple.add(attributeRow);
        }
        return attributeTuple;
    }

    public void writeTable(String tableName) {
        String fileContent = mTableFileControl.readFile(SUBS_TABLES);
        System.out.println(fileContent);

        int length = fileContent.split("\\$_\\$").length;
        for (int i=0;i<length;i++){
            if (fileContent.split("\\$_\\$")[i].split(":")[0].equals(tableName)){
                System.out.println(tableName+"表已经存在");
                return;
            }
        }
        System.out.println(tableName+"已经添加成功");
        String content = tableName + ":" + tableName + ".txt$_$";
        mTableFileControl.writeToFile(SUBS_TABLES, content);

    }


}
