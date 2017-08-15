package wq.votzone.notebook.util

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipData
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/29
 * Modification History:
 * Why & What modified:
 */
class GlobalUtil {
}


fun dip2px(context: Context, dipValue: Float): Int {
    val scale = context.getResources().getDisplayMetrics().density
    return (dipValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.getResources().getDisplayMetrics().density
    return (pxValue / scale + 0.5f).toInt()
}

fun showToast(context: Context, msg:String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun put2ClipboardOnly(context: Context, msg:String){
    val myClipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    if(myClipboard != null){
        val myClip: ClipData
        myClip = ClipData.newPlainText("text", msg)
        myClipboard.primaryClip = myClip
    }
}

fun put2Clipboard(context: Context, msg:String){
    val myClipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    if(myClipboard != null){
        val myClip: ClipData
        myClip = ClipData.newPlainText("text", msg)
        myClipboard.primaryClip = myClip
        showToast(context, "已复制到剪切板")
    }
}




