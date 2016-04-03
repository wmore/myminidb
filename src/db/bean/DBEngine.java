package db.bean;

/**
 * Created by wangyue on 16/3/25.
 */

import db.filecontrol.MTableFileControl;
import sun.rmi.runtime.Log;
import util.LogToll;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


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
    final String SUBS_TABLES_ATTRIBUTE = "SUBS_TABLES_ATTRIBUTE.txt";

    public DBEngine() {
    }

    //建表
    public void createTable(String tableName, ArrayList<String> attribute_types, ArrayList<String> attribute_names) {
        String fileContent = mTableFileControl.readFile(SUBS_TABLES);
        System.out.println(fileContent);

        int length = fileContent.split("@_@").length;
        for (int i = 0; i < length; i++) {
            if (fileContent.split("@_@")[i].split(":")[0].equals(tableName)) {
                System.out.println(tableName + "表已经存在");
                return;
            }
        }
        System.out.println(tableName + "已经添加成功");
        String content = tableName + ":" + tableName + ".txt@_@";
        mTableFileControl.writeToFile(SUBS_TABLES, content);

        MTable newTable = new MTable(tableName, attribute_types, attribute_names);
        mTableFileControl.writeToFile(SUBS_TABLES_ATTRIBUTE, tableName + ":");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attribute_types.size(); i++) {
            sb.append(attribute_names.get(i) + "," + attribute_types.get(i));
            if (i != attribute_types.size() - 1)
                sb.append("@_@");
        }
        sb.append(";");
        mTableFileControl.writeToFile(SUBS_TABLES_ATTRIBUTE, sb.toString());
        System.out.println("表元素有:" + mTableFileControl.readFile(SUBS_TABLES_ATTRIBUTE));
//        tablesMap.put(name, newTable);
    }

    //查表
    public ArrayList<ArrayList<String>> queryAttributeTuple(String tableName) {
        if (!hasTable(tableName)) {
            return null;
        }
        //从表数据文件里获取数据
        String tableContent = mTableFileControl.readFile(findTableFile(tableName));
        System.out.println(tableContent);
        int length = tableContent.split(";").length;

        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < length - 1; i++) {
            ArrayList<String> attributeRow = new ArrayList<String>();
            String subContent = tableContent.split(";")[i];
            int length2 = subContent.split("@_@").length;
            for (int j = 0; j < length2; j++) {
                attributeRow.add(subContent.split("@_@")[j]);
            }
            attributeTuple.add(attributeRow);
        }
        return attributeTuple;
    }

    /**
     * 根据条件查询表数据
     *
     * @param tableName
     * @return
     */
    public ArrayList<ArrayList<String>> queryAttributeTupleByCondition(String tableName, ArrayList<String> conAttrNames,
                                                                       ArrayList<String> conAttrValues) {
        if (!hasTable(tableName)) {
            return null;
        }
        //从表数据文件里获取数据
//        String tableContent = mTableFileControl.readFile(findTableFile(tableName));
//        System.out.println(tableContent);
//        int length = tableContent.split(";").length;

//        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();
//        for (int i = 0; i < length - 1; i++) {
//            ArrayList<String> attributeRow = new ArrayList<String>();
//            String subContent = tableContent.split(";")[i];
//            int length2 = subContent.split("@_@").length;
//            for (int j = 0; j < length2; j++) {
//                attributeRow.add(subContent.split("@_@")[j]);
//            }
//            attributeTuple.add(attributeRow);
//        }

        ArrayList<String[]> structure = getTableStructure(tableName);
        ArrayList<Integer> attrNamesIndexList = getAttrNameIndex(conAttrNames, structure);
        LogToll.printLog("queryAttributeTupleByCondition", "All attrbute index are queried are:" + attrNamesIndexList);
        //从表数据文件里获取数据
        String tableContent = mTableFileControl.readFile(findTableFile(tableName));

        System.out.println(tableContent);


        String[] splitArray = tableContent.split(";");
        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();

        boolean finded = true;

        for (int i = 0; i < splitArray.length - 1; i++) {
            String subContent = splitArray[i];
            String[] splitValues = subContent.split("@_@");
            for (int j = 0; j < attrNamesIndexList.size(); j++) {
                if (conAttrValues.indexOf(splitValues[j]) != j) {
                    finded = false;
                    break;
                }
            }
            if (finded) {
                List<String> list = Arrays.asList(splitValues);
                ArrayList<String> attributeRow = new ArrayList<String>(list.size());
                attributeRow.addAll(list);
                attributeTuple.add(attributeRow);
            }
            finded = true;
        }


        return attributeTuple;
    }

    //从找到tableName对应的文件名
    public String findTableFile(String tableName) {

        String tableFileName = null;

        String subsTablesContent = mTableFileControl.readFile(SUBS_TABLES);
        String[] tablesArray = subsTablesContent.split("@_@");

        for (int i = 0; i < tablesArray.length; i++) {
            String[] tableElement = tablesArray[i].split(":");
            if (tableName.equals(tableElement[0])) {
                tableFileName = tableElement[1];
            }
        }

        return tableFileName;
    }

    //插入数据
    public boolean insertVaulesTable(String tableName, ArrayList<String> attrNames, ArrayList<String> attrValues) {
        if (!hasTable(tableName)) {
            return false;
        }
        StringBuilder newContent = new StringBuilder();
        ArrayList<String[]> structure = getTableStructure(tableName);
        //insert int table values (xxx,xxx,xxx);不带字段名字的情况,全量插入
        if (attrValues.size() != structure.size()) {
            ArrayList<String> newValues = new ArrayList<String>(structure.size());
            for (int i = 0; i < structure.size(); i++) {
                for (int j = 0; j < attrNames.size(); j++) {
                    if (structure.get(i)[0].equals(attrNames.get(j)))
                        newValues.set(i, attrValues.get(j));
                }
            }
            attrValues = newValues;
        }

        for (int i = 0; i < attrValues.size(); i++) {
            newContent.append(attrValues.get(i));
            if (attrValues.size() - 1 != i)
                newContent.append("@_@");
        }
        newContent.append(";");
        mTableFileControl.writeToFile(findTableFile(tableName), newContent.toString());
        return true;
    }


    //获得表字段结构
    public ArrayList<String[]> getTableStructure(String tableName) {
        if (!hasTable(tableName)) {
            return null;
        }
        ArrayList<String[]> attrStructure = new ArrayList<String[]>();
        //table:attr1,CHAR@_@;
        //获取表字段结构  名字和类型的列表
        for (String tableElem : mTableFileControl.readFile(SUBS_TABLES_ATTRIBUTE).split(";")) {
            if (tableElem.split(":")[0].equals(tableName)) {
                for (String attrElem : tableElem.split(":")[1].split("@_@")) {
                    System.out.println(attrElem);
                    attrStructure.add(new String[]{attrElem.split(",")[0], attrElem.split(",")[1]});
                }
            }
        }
        return attrStructure;
    }

    //删除所有表数据
    public boolean truncateTable(String tableName) {
        LogToll.printLog("truncateTable", "开始删除表" + tableName);
        if (hasTable(tableName)) {
            return mTableFileControl.deleteFile(findTableFile(tableName));
        }
        return false;
    }

    //检查表是否存在
    public boolean hasTable(String tableName) {
        String tableFileName = findTableFile(tableName);
        if (tableFileName == null) {
            System.out.println("the table " + tableName + "is not exist!");
            return false;
        }
        return true;
    }

    //删除表数据
    public boolean deleteValues(String tableName, ArrayList<String> attrNames, ArrayList<String> attrValues) {

        StringBuilder newContent = new StringBuilder();
        ArrayList<String[]> structure = getTableStructure(tableName);

        ArrayList<Integer> attrNamesIndexList = getAttrNameIndex(attrNames, structure);

        LogToll.printLog("deletevalues", "All attrbute index will be deleted are:" + attrNamesIndexList);
        //从表数据文件里获取数据
        String tableContent = mTableFileControl.readFile(findTableFile(tableName));

        System.out.println(tableContent);

        int length = tableContent.split(";").length;

        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();

        boolean finded = true;

        for (int i = 0; i < length - 1; i++) {
            ArrayList<String> attributeRow = new ArrayList<String>();
            String subContent = tableContent.split(";")[i];
            String[] splitValues = subContent.split("@_@");
            for (int j = 0; j < attrNamesIndexList.size(); j++) {
                if (attrValues.indexOf(splitValues[j]) != j) {
                    finded = false;
                    break;
                }
            }
            //删除
            if (finded) {
                String deletedStr = subContent + ";";
                LogToll.printLog("deletevalues", "whole data before delete is : " + tableContent);
                LogToll.printLog("deletevalues", "the String that will be deleted is " + deletedStr);
//                tableContent.replace(deletedStr,"");
                mTableFileControl.deleteString(findTableFile(tableName), deletedStr);
                LogToll.printLog("deletevalues", "whole data before delete is : " + tableContent);
            }
            finded = true;
        }


        return true;
    }

    /**
     * 根据表结构查attrname顺序列表
     *
     * @param searchAttrNames
     * @param structure
     * @return
     */
    public ArrayList<Integer> getAttrNameIndex(ArrayList<String> searchAttrNames, ArrayList<String[]> structure) {

        ArrayList<Integer> attrNamesIndexList = new ArrayList<Integer>();
        LogToll.printLog("deletevalues", "All attrbute name will be deleted are: " + searchAttrNames);
        for (String attrName : searchAttrNames) {
            for (int i = 0; i < structure.size(); i++) {
                if (structure.get(i)[0].equals(attrName))
                    attrNamesIndexList.add(i);
            }
        }
        return attrNamesIndexList;
    }

    //修改表数据
    public boolean updateValues(String tableName, ArrayList<String> conAttrNames, ArrayList<String> conAttrValues,
                                ArrayList<String> updateObjectAttrNames, ArrayList<String> updateObjectAttrValues) {
        StringBuilder oldContent = null;
        StringBuilder newContent = null;
        ArrayList<String[]> structure = getTableStructure(tableName);

        ArrayList<Integer> conAttrNamesIndexList = getAttrNameIndex(conAttrNames, structure);
        ArrayList<Integer> updateAttrNamesIndexList = getAttrNameIndex(updateObjectAttrNames, structure);

        LogToll.printLog("deletevalues", "All attrbute index will be deleted are:" + conAttrNamesIndexList);
        //从表数据文件里获取数据
        String tableContent = mTableFileControl.readFile(findTableFile(tableName));

        System.out.println(tableContent);

        ArrayList<ArrayList<String>> attributeTuple = new ArrayList<ArrayList<String>>();

        boolean finded = true;

        String[] splitArray = tableContent.split(";");

        for (int i = 0; i < splitArray.length - 1; i++) {
            ArrayList<String> attributeRow = new ArrayList<String>();
            String subContent = splitArray[i];

            String[] splitValues = subContent.split("@_@");

            for (int j = 0; j < conAttrNamesIndexList.size(); j++) {
                if (conAttrValues.indexOf(splitValues[j]) != j) {
                    finded = false;
                    break;
                }
            }
            if (finded) {
                oldContent = new StringBuilder();
                newContent = new StringBuilder();

                for (int l = 0; l < splitValues.length; l++) {
                    oldContent.append(splitValues[l]);
                    if (splitValues.length - 1 != l)
                        oldContent.append("@_@");
                }
                for (int k = 0; k < updateAttrNamesIndexList.size(); k++) {
                    splitValues[updateAttrNamesIndexList.get(k)] = updateObjectAttrValues.get(k);
                }

                for (int l = 0; l < splitValues.length; l++) {
                    newContent.append(splitValues[l]);
                    if (splitValues.length - 1 != l)
                        newContent.append("@_@");
                }

                LogToll.printLog("updatevalues", "the string data before update is : " + oldContent);
                LogToll.printLog("updatevalues", "the String that will be update to  " + newContent);
                mTableFileControl.updateString(findTableFile(tableName), oldContent.toString(), newContent.toString());
                LogToll.printLog("deletevalues", "whole data before delete is : " + tableContent);
            }
            finded = true;
        }


        return true;


    }
}
