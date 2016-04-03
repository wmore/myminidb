package db.filecontrol;

import util.LogToll;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by wangyue on 16/3/26.
 */
public class MTableFileControl {

    final String FILEPATH = "/Users/wangyue/IdeaProjects/studyjava/myminidb/files/";

    public void writeToFile(String fileName, String content) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILEPATH + fileName, true);
            fw.append(content);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName) {
        File file = new File(FILEPATH + fileName);
        return file.delete();
    }

    public String readFile(String fileName) {
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        String tempString = null;
        try {
            File file = new File(FILEPATH + fileName);
            if (!file.exists())
                file.createNewFile();
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            try {
                while ((tempString = br.readLine()) != null) {
                    content.append(tempString).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (br != null)
                br.close();
            if (fr != null)
                fr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public boolean deleteString(String fileName,String deletedStr){
        return updateString(fileName,deletedStr,"");
    }

    public boolean updateString(String fileName,String oldContent,String newContent){
        String fileContent = readFile(fileName);
        deleteFile(fileName);
        writeToFile(fileName,fileContent.replaceAll(oldContent,newContent));
        return true;
    }


}
