package JavaChat.Common.Utils;

import javafx.scene.image.Image;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 该工具类内含有一些静态方法,可用于将文件保存至磁盘
 */
public class FileUtils {
    //实现将path中的文件提取出来
    public static File GetFile(String Path) throws Exception{
        return new File(Path);
    }

    /**
     * 该类实现将文件存入到服务器磁盘当中并返回在磁盘中的地址
     * @param file
     * @return
     * @throws Exception
     */
    public static String SetFile(File file) throws Exception{
        File file1 =null;
        try {
            file1 = GetFile(ServerFileAddress.File);
            FileUtils.copy(file,file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file1 + "\\" +file.getName();
    }

    /**
     * 实现将file文件转换为javafx中的Image
     * @param file
     * @return
     */
    public static Image File_Image(File file){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = null;
        image = new Image(inputStream);
        return image;
    }

    /**
     * 此类实现将文件f复制到指定路径f1当中
     * @param f
     * @param f1 文件路径
     * @throws IOException
     */
    public static void copy(File f,File f1) throws IOException{ //复制文件的方法!
        if(!f1.exists()){
            f1.mkdir();
        }
        if(!f1.exists()){//路径判断，是路径还是单个的文件
            File[] cf = f.listFiles();
            for(File fn : cf){
                if(fn.isFile()){
                    FileInputStream fis = new FileInputStream(fn);
                    FileOutputStream fos = new FileOutputStream(f1 + "\\" +fn.getName());
                    byte[] b = new byte[1024];
                    int i = fis.read(b);
                    while(i != -1){
                        fos.write(b, 0, i);
                        i = fis.read(b);
                    }
                    fis.close();
                    fos.close();
                }else{
                    File fb = new File(f1 + "\\" + fn.getName());
                    fb.mkdir();
                    if(fn.listFiles() != null){//如果有子目录递归复制子目录!
                        copy(fn,fb);
                    }
                }
            }
        }else{
            FileInputStream fis = new FileInputStream(f);
            FileOutputStream fos = new FileOutputStream(f1 + "\\" +f.getName());
            byte[] b = new byte[1024];
            int i = fis.read(b);
            while(i != -1){
                fos.write(b, 0, i);
                i = fis.read(b);
            }
            fis.close();
            fos.close();
        }
    }
    /**
     * 该类提供将一个File对象转换为字节数组
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] FileChangeToByte(File file) throws Exception{
        FileInputStream fis = null;
        byte[] filebytes = new byte[(int)file.length()];
        fis = new FileInputStream(file.getAbsolutePath());
        fis.read(filebytes);
        if(fis!=null) {
            fis.close();
        }
        return filebytes;
    }

    /**
     * 把byte[]⽂件流，指定和⽂件名，保存在指定路径
     * @param bfile 保存的⽂件流
     * @param filePath 保存路径
     * @param fileName 保存⽂件
     */
    public static String saveFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        File newfolder = new File(filePath);
        if(newfolder.mkdir()){
            System.out.println("创建一级目录成功");
        }
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断⽂件⽬录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }
    /**
     * 该类以上传时间作为文件名将字节数组中的内容保存至磁盘并可返回file文件的绝对路径
     * @param bfile
     * @param filePath
     * @return
     */
    public static String saveFile(byte[] bfile, String filePath) {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = sdf.format(System.currentTimeMillis());
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断⽂件⽬录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName+".jpg");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static String getFileType(File file){
        String mimeType = null;
        try {
            Path path = file.toPath();
            mimeType = Files.probeContentType(path);
            System.out.println(mimeType);
            return mimeType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }

    /**
     * test
     */
    public static void main(String[] args) {
        try {
            loadFiles("D:\\ClientPrivatePath\\File\\9929651220220529181534Java网络编程pptx","D:\\ClientPrivatePath\\File\\9929651220220529181534Java网络编程pptx","编程.pptx");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将Object对象转byte数组
     * @param obj byte数组的object对象
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }


    /**
     * 将byte数组按照指定大小分割成多个数组
     * @param bytes   要分割的byte数组
     * @param subSize  分割的块大小  单位：字节
     * @return 指定大小的byte数组
     */
    public static Object[] splitByteArr(byte[] bytes, int subSize) {
        int count = bytes.length % subSize == 0 ? bytes.length / subSize : bytes.length / subSize + 1;
        List<List<Byte>> subAryList = new ArrayList<List<Byte>>();
        for (int i = 0; i < count; i++) {
            int index = i * subSize;
            List<Byte> list = new ArrayList<Byte>();
            int j = 0;
            while (j < subSize && index < bytes.length) {
                list.add(bytes[index++]);
                j++;
            }
            subAryList.add(list);
        }

        Object[] subAry = new Object[subAryList.size()];

        for (int i = 0; i < subAryList.size(); i++) {
            List<Byte> subList = subAryList.get(i);
            byte[] subAryItem = new byte[subList.size()];
            for (int j = 0; j < subList.size(); j++) {
                subAryItem[j] = subList.get(j);
            }
            subAry[i] = subAryItem;
        }
        return subAry;
    }

    //将byte数组存到客户端地址,并将file文件返回
    public static File ByteToFile(byte[] bytes,String Address,String name){
        String path = FileUtils.saveFile(bytes,Address,name);
        File file = new File(path);
        return file;
    }

    public static boolean CheckFileIs_png_jpeg(File file){
        String type = getFileType(file);
        return type.equals("image/png")||type.equals("image/jpeg");
    }

    /**
     * 拼接字符数组
     * @param byte_1
     * @param byte_2
     * @return
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static boolean CheckFileIsExists(String path){
        File file = new File(path);
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public static int numberOfFiles(String path){
        File folder = new File(path);
        File []list = folder.listFiles();
        int fileCount = 0, folderCount = 0;
        long length = 0;
        for (File file : list){
            if (file.isFile()){
                fileCount++;
                length += file.length();
            }else {
                folderCount++;
            }
        }
        //System.out.println("文件夹的数目: " + folderCount + " 文件的数目: " + fileCount);
        return fileCount;
    }

    /**
     * 将目录中的文件整合
     * @param filePath
     * @param destPath
     * @throws IOException
     */
    public static void loadFiles(String filePath,String destPath,String name) throws IOException {
        File[] files = new File(filePath).listFiles();
        int length = files.length;
        File dir = new File(filePath);
        if(!dir.exists()&&dir.isDirectory()){
            dir.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(destPath+"\\"+name,true);
        byte[] bytes = new byte[0];
        for (int i = 1 ; i <= length ; i++){
            try {
                bytes = FileChangeToByte(new File(filePath+"\\"+((Integer)i).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos.write(bytes);
        }
        fos.close();
    }
}
