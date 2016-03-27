package db.bean;

/**
 * Created by wangyue on 16/3/25.
 */

import java.util.ArrayList;
import java.util.HashMap;


/**************
 {
 "name":"tableName",
 "attritubeList":[
 {
 "attributeName": "name",
 "attributeType": "type",
 "isPrimaryKey": "true or false",
 "primaryKeyValue": "value"
 },
 {
 "attributeName": "name",
 "attributeType": "type",
 "length": "length ",
 "isPrimaryKey": "true or false",
 "primaryKeyValue": "value"
 }
 ],
 "rows":[
 [" "," ", " "],
 [" "," ", " "]
 ],
 }
 ******************/
public class DBEngine {
    private HashMap<String,MTable> tablesMap = new HashMap<String, MTable>();

    public DBEngine() {}

    public void createTable(String name, ArrayList<String> attribute_types,ArrayList<String> attribute_names){
        MTable newTable = new MTable(name,attribute_types,attribute_names);
        tablesMap.put(name,newTable);
    }

    public void queryTable(String name){
        MTable table ;

    }

    public void writeTable(){

    }

}
