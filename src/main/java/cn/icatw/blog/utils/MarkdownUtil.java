package cn.icatw.blog.utils;

import com.itextpdf.text.DocumentException;
import com.youbenzi.md2.export.FileFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author 王顺 762188827@qq.com
 * @apiNote
 * @since 2024/4/17
 */
public class MarkdownUtil {
    public static void main(String[] args) throws DocumentException, IOException {
    }

    public static byte[] convertMarkdownToByte(String markdownContent) throws IOException, DocumentException {
        // 创建临时目录
        Path tempDirPath = Files.createTempDirectory("tempDir");
        String tempDirAbsolutePath = tempDirPath.toAbsolutePath().toString();
        System.out.println("临时目录路径：" + tempDirAbsolutePath);

        // 在临时目录中创建文件或进行其他操作
        String tempFilePath = tempDirAbsolutePath +  System.currentTimeMillis() + ".pdf";
        Files.createFile(Path.of(tempFilePath));
        //String resourceDirectoryPath = ResourceUtils.getFile("classpath:").getAbsolutePath();
        // 创建导出文件的路径
        //创建不重复的文件名
        FileFactory.produce(markdownContent, tempFilePath);
        System.out.println("导出路径：" + tempFilePath);
        return getBytesByFile(tempFilePath);
    }

    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把一个文件转化为byte字节数组。
     *
     * @return
     */
    private static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}
