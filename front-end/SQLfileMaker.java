package test.jdw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SQLfileMaker {
    static public void main(String[] args) {
        
        // 书本和出版社数据读取
        Set <String> data = readFileByLines("C:\\Workspace\\books.txt");
        
        Iterator<String> dataIter = data.iterator();
        Iterator<String> dataIter1 = data.iterator();
        String[] strData = null;
        
        Set <String> publishers = new HashSet <String>();
        Set <String> booksFile = new HashSet <String>();
        
        while(dataIter.hasNext()) {
            strData = dataIter.next().toString().split("#");
            publishers.add(strData[1]);
        }
               
        // 编写出版社数据SQL插入语句       
        Map <String, String> publisherMap = new HashMap <String, String>();
        Set <String> publishersFile = new HashSet <String>();
        
        Iterator<String> it = publishers.iterator();
        int publisherID = 2001;
        while(it.hasNext()) {
            String str = it.next().toString();
            publisherMap.put(""+publisherID, str);
            String temp = "INSERT INTO publisher (name,address,tel,fax,seq_publisher_id) VALUES ('" + str + "','','','','" + publisherID + "');";
            publishersFile.add(temp);
            publisherID ++ ;
        }
        
        while(dataIter1.hasNext()) {
            strData = dataIter1.next().toString().split("#");
            String publisher_id = getPublisherID(publisherMap, strData[1]);
            String temp = "INSERT INTO book (isbn,author,translator,title,price,seq_publisher_id) VALUES (" + 
                          strData[0] + ",'" + publisher_id + "');";
            booksFile.add(temp);
        }
        
             
        // 输出出版社数据SQL语句文件
        writeIntoFile("C:\\Workspace\\insertPublisher.txt", publishersFile);
        writeIntoFile("C:\\Workspace\\insertBook.txt", booksFile);
        
        System.out.println("文件作成完成！");
    }
    
    
    private static String getPublisherID(Map<String, String> publisherMap, String publish) {
        
        for(Iterator<Entry<String, String>> iter = publisherMap.entrySet().iterator();iter.hasNext();){
            Map.Entry<String, String> element = (Map.Entry<String, String>)iter.next();
            
            if(element.getValue().equals(publish)) {
                return element.getKey();
            }
          }
        return "";
    }
    
    
    public static Set<String> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        
        Set<String> list = new HashSet<String>();
        String tempString = null;
        
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8"); 
            reader = new BufferedReader(isr);
            while((tempString=reader.readLine())!=null) {
                list.add(tempString);
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null) {
                try {
                    reader.close();
                } catch(IOException e) {
                    
                }
            }
        }
        return list;
    }
    
    public static void writeIntoFile(String fileName, Set<String> results) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            
            for(String temp: results) {
                writer.write(temp + System.getProperty("line.separator"));
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
