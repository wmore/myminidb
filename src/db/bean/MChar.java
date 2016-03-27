package db.bean;

/**
 * Created by wangyue on 16/3/25.
 */
public class MChar {
    protected int length;
    protected String value;

    public MChar(int length) {
        this.length = length;
    }

    public boolean inputValue(String value) {
        if(value.length() > length){
            //throw exception(recoverable)
        } else{
            this.value = value;
        }
        return true;
    }
}
