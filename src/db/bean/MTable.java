package db.bean;


import java.util.ArrayList;

/**
 * Created by wangyue on 16/3/25.
 */
public class MTable {
    private String tableName;
    private ArrayList<String> fieldType = new ArrayList<String>();
    private ArrayList<String> fieldName = new ArrayList<String>();
    private ArrayList<MRow> rowList;
    private String tableFilePath;

    public MTable(String tableName, ArrayList<String> fieldType, ArrayList<String> fieldName) {
        this.tableName = tableName;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }

    public void addAttribute(String type, String name) {
        this.fieldType.add(type);
        this.fieldName.add(name);
    }

    public ArrayList<MRow> findRowsByField(String name, String value) {
        ArrayList<MRow> rows  = new ArrayList<MRow>();
        MRow row;
        int index = 0;
        for (int i = 0; i < this.fieldName.size(); i++) {
            if (this.fieldName.get(i).equals(name)) {
                index = i;
                break;
            }
        }
        for (int i = 0;i<this.rowList.size();i++){
            row = rowList.get(i);
            if (row.getFieldVaules().get(index).equals(value)){
                rows.add(row);
            }
        }
        return rows;
    }
}
