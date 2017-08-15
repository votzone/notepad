package wq.votzone.notebook.util;

import android.content.Context;
import android.os.Environment;

import org.xutils.common.util.MD5;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import wq.votzone.notebook.NotApplication;
import wq.votzone.notebook.ex.WrongKeyException;

import static wq.votzone.notebook.NotApplicationKt.getApplication;

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/29
 * Modification History:
 * Why & What modified:
 */

public class FileUtils {

//    private static String key = "123456";
    public static void saveWithEncode(File file, String content) throws IOException{
        FileOutputStream fos = new FileOutputStream(file);
        content += getKeyVerify(getApplication().getKey());
        byte[] bs = encodeStr(content);
        fos.write(ByteUtil.intToByte4(bs.length));
        fos.write(bs);
        fos.flush();
        fos.close();
    }

    public static String decodeFile2Jstr(File file) throws IOException, WrongKeyException{

        return decodeFile2JstrWithKey(file, getApplication().getKey());
    }

    public static String decodeFile2JstrWithKey(File file, String key) throws IOException, WrongKeyException{
        String content = "";
        FileInputStream fis = new FileInputStream(file);
        byte[] lbs = new byte[4];
        fis.read(lbs);
        int lenght = ByteUtil.byte4ToInt(lbs);
        byte[] cbs = new byte[lenght];
        fis.read(cbs);
        content =  decodeBytes(cbs,key);
        String verify = getKeyVerify(key);
        if(content.endsWith(verify)){
            return content.substring(0,content.length() -verify.length());
        }else {
            throw new WrongKeyException();
        }
    }


    private static String getKeyVerify(String key){
        return MD5.md5(key);
    }

    private static byte getKeyXorKey(String key){
        byte[] bs = key.getBytes();
        byte xk = 0;
        for(byte b: bs){
            xk ^=b;
        }
        return xk;
    }

    private static byte[] encodeStr(String content){
        byte[] bs = content.getBytes();
        byte xk = getKeyXorKey(getApplication().getKey());
        for (int i=0;i<bs.length;i++){
            bs[i]^=xk;
        }
        return bs;
    }

    private static String decodeBytes(byte[] bs, String key){
        byte xk = getKeyXorKey(key);
        for (int i=0;i<bs.length;i++){
            bs[i]^=xk;
        }
        return new String(bs);
    }

    public static File getExportDir(){
        File dir =  new File(Environment.getExternalStorageDirectory(), "NotePad");
        dir.mkdirs();
        return dir;
    }

    public static File getExportPath(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String filename = formatter.format(new Date());
        return new File(getExportDir(),filename+".export");
    }
}

/**
* fun getExportPath(context: Context):File{
    var dir = context.getExternalFilesDir("export")
    val formatter= SimpleDateFormat ("yyyyMMddHHmmss")
    val filename = formatter.format(Date())
    var outfile =  File(dir, filename+".export")
    Log.e("exportPath2",outfile.absolutePath)
    return outfile
}
* */
