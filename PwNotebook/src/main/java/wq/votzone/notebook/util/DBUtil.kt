package wq.votzone.notebook.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

import org.xutils.DbManager
import org.xutils.ex.DbException
import org.xutils.x

import java.io.File

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/5/16
 * Modification History:
 * Why & What modified:
 */

object DBUtil {

    private val daoConfig = DbManager.DaoConfig()
            .setDbName("vot_note_pad.db")
//            .setDbDir(File("/sdcard/Android/data/wq.votzone.notebook/"))
            .setDbVersion(1)
            .setDbOpenListener { db -> db.database.enableWriteAheadLogging() }
            .setDbUpgradeListener { db, oldVersion, newVersion -> }

    private var db: DbManager? = null
    val dbManager: DbManager?
        get() {
            if (db == null) {
                db = x.getDb(daoConfig)
            }
            return db
        }
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun verifyStoragePermissions(activity: Activity): Boolean {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE)
            return false
        }
        return true
    }

    fun saveBindingId(entity: Any): Boolean {

        val db = dbManager
        try {
            return db!!.saveBindingId(entity)
        } catch (e: DbException) {
            return false
        }

    }

    fun update(entity: Any):Boolean{
        val db = dbManager

        try {
            db!!.saveOrUpdate(entity)
        } catch (e:DbException){
            return false
        }
        return true
    }

    fun delete(entity: Any) {
        val db = dbManager
        try {
            db!!.delete(entity)
        } catch (e: DbException) {

        }

    }

    fun <T> findAll(entityType: Class<T>, orderBy: String, desc: Boolean): List<T>? {
        val db = dbManager

        try {
            return db!!.selector(entityType).orderBy(orderBy, desc).findAll()
        } catch (e: DbException) {
            return null
        }

    }

    fun<T> findList(entityType: Class<T>, column: String, value: Any):List<T>?{
        val db = dbManager
        //"parentId", "=", this.id
        try {
           return  db!!.selector(entityType).where(column,"=", value).findAll()
        }catch (e:DbException){
            return null;
        }

    }

    fun<T> findOne(entityType: Class<T>, column: String, value: Any):T?{
        val db = dbManager
        //"parentId", "=", this.id
        try {
            return  db!!.selector(entityType).where(column,"=", value).findFirst()
        }catch (e:DbException){
            return null;
        }

    }


}
