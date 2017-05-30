package code.ponfee.pay;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressDeflater {

    // Export only static methods  
    private CompressDeflater() {}

    public static byte[] compress(byte[] value, int offset, int length, int compressionLevel) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);

        Deflater compressor = new Deflater();

        try {
            compressor.setLevel(compressionLevel); //将当前压缩级别设置为指定值。  
            compressor.setInput(value, offset, length);
            compressor.finish(); //调用时，指示压缩应当以输入缓冲区的当前内容结尾。  

            // Compress the data  
            final byte[] buf = new byte[1024];
            while (!compressor.finished()) {
                //如果已到达压缩数据输出流的结尾，则返回 true。  
                int count = compressor.deflate(buf);
                // 使用压缩数据填充指定缓冲区。  
                bos.write(buf, 0, count);
            }
        } finally {
            compressor.end(); //关闭解压缩器并放弃所有未处理的输入。  
        }

        return bos.toByteArray();
    }

    public static byte[] compress(byte[] value, int offset, int length) {
        return compress(value, offset, length, Deflater.BEST_COMPRESSION);
        // 最佳压缩的压缩级别  
    }

    public static byte[] compress(byte[] value) {
        return compress(value, 0, value.length, Deflater.BEST_COMPRESSION);
    }

    public static byte[] decompress(byte[] value) throws DataFormatException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(value.length);
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(value);
            final byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            decompressor.end();
        }
        return bos.toByteArray();
    }
    
    // 测试方法   
 public static void main(String[] args) throws IOException, DataFormatException {

     //测试字符串   
     String str = "%5B%7B%22lastUpdateTime%魂牵梦萦城22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";

     System.out.println("原长度：" + str.getBytes().length);

     System.out.println("压缩后：" + compress(str.getBytes()).length);

     System.out.println("解压缩：" + decompress(compress(str.getBytes())).length);
 }

}