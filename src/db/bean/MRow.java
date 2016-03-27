package db.bean;

import java.util.ArrayList;

/**
 * Created by wangyue on 16/3/25.
 */
public class MRow {
    private String rowId;
    private ArrayList<String> fieldVaules = new ArrayList<String>();

    public ArrayList<String> getFieldVaules() {
        return fieldVaules;
    }

    public void setFieldVaules(ArrayList<String> fieldVaules) {
        this.fieldVaules = fieldVaules;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
}
