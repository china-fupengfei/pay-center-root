package code.ponfee.pay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;  
  
  
/** 
 *  
 * @author wenqi5 
 *  
 */  
public class CompressGzip {  
  
    public static byte[] gzip(byte[] content) throws IOException{  
        ByteArrayOutputStream baos=new ByteArrayOutputStream();  
        GZIPOutputStream gos=new GZIPOutputStream(baos);  
          
        ByteArrayInputStream bais=new ByteArrayInputStream(content);  
        byte[ ] buffer=new byte[1024];  
        int n;  
        while((n=bais.read(buffer))!=-1){  
            gos.write(buffer, 0, n);  
        }  
        gos.flush();  
        gos.close();  
        return baos.toByteArray();  
    }  
      
    public static byte[] unGzip(byte[] content) throws IOException{  
        ByteArrayOutputStream baos=new ByteArrayOutputStream();  
        GZIPInputStream gis=new GZIPInputStream(new ByteArrayInputStream(content));  
        byte[] buffer=new byte[1024];  
        int n;  
        while((n=gis.read(buffer))!=-1){  
            baos.write(buffer, 0, n);  
        }  
          
        return baos.toByteArray();  
    }  
  
    public static void main(String[] args) throws IOException {  
        byte[] data = "附近的卡萨副驾驶的aaaaaaaaaaaaaaaaaaaa卡拉飞进来的设计方老师姐夫附近看到拉斯减肥咖啡就开始拉房价开始多了几分fdsa".getBytes();  
        System.out.println("原长度：" + data.length);  
        byte[] gzip = gzip(data);
        System.out.println("压缩后字符串：" + gzip.length);  
        byte[] ungzip = unGzip(gzip);
        System.out.println("解压缩后字符串长度：" + ungzip.length);
        System.out.println("解压缩后字符串：" + new String(ungzip));  
    }  
}  