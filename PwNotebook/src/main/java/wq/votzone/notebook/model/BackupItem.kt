package wq.votzone.notebook.model

import android.content.Context
import wq.votzone.notebook.activity.ImportActivity
import wq.votzone.notebook.util.FileUtils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/8/1
 * Modification History:
 * Why & What modified:
 */
class BackupItem (file: File = File("def")){

    var fileName:String = ""
    var filePath:String = ""
    var lastModify: Long = 0L
    var createTime:String = ""

    lateinit var innerfile:File

    init {
        initWith(file)
    }

    fun deleteFile(){
        innerfile.delete()
    }

    fun decodeFile():String{
        return FileUtils.decodeFile2Jstr(innerfile)
    }

    fun decodeFileWithKey( key:String):String{
        return FileUtils.decodeFile2JstrWithKey(innerfile, key)
    }

    fun initWith(file:File){
        innerfile = file;
        fileName = file.name
        filePath = file.parentFile.absolutePath

        var now = System.currentTimeMillis()
        lastModify = file.lastModified()
        var modifyDate = Date(lastModify)

        var modifyCal = Calendar.getInstance()
        modifyCal.time = modifyDate

        var curCal = Calendar.getInstance()

        var curMill =System.currentTimeMillis()
        var dayLong = 1000 * 60 * 60 * 24
        if(curMill - lastModify <  dayLong*2){
            //48小时之内
            if(curCal.get(Calendar.DAY_OF_YEAR) == modifyCal.get(Calendar.DAY_OF_YEAR)){
                createTime = "今天"
            }else if (curCal.get(Calendar.DAY_OF_YEAR) == modifyCal.get(Calendar.DAY_OF_YEAR) +1){
                createTime = "昨天"
            }else{
                createTime = "七天内"
            }
        }else if(curMill - lastModify < dayLong *7){
            createTime = "七天内"
        }else if(curMill - lastModify < dayLong * 30){
            createTime = "30天内"
        }else if(curMill - lastModify < dayLong * 365){
            createTime = "一年内"
        }else{
            createTime = "很久以前"
        }

    }

    companion object {

        fun getBackupItemList():ArrayList<BackupItem> {
            var list = ArrayList<BackupItem>()
            var dir = FileUtils.getExportDir()
            var files = dir.listFiles()
            if(files !=null){
                for(f in files){
                    if(f.isFile || f.absolutePath.endsWith(".export")) {
                        list.add(BackupItem(f))
                    }

                }
            }
            return list
        }
    }
}