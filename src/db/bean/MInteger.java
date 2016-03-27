package db.bean;

/**
 * Created by wangyue on 16/3/25.
 */
public class MInteger{
    protected int length;
    protected int value;

    public MInteger(int length) {
        this.length = length;
    }

    public boolean inputInteger(int value){

        if(String.valueOf(value).length()>length){
            System.out.println("超出长度了");
            return false;
        }else{
            this.value = value;
            return true;
        }
    }

    public int getLength() {
        return length;
    }
}
