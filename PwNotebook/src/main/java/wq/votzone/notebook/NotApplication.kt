package wq.votzone.notebook

import android.app.Application
import com.umeng.analytics.MobclickAgent

import org.xutils.x
import org.xutils.DbManager
import wq.votzone.notebook.util.PrefUtil
import java.io.File


/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/1
 * Modification History:
 * Why & What modified:
 */

class NotApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        x.Ext.init(this)
        x.Ext.setDebug(BuildConfig.DEBUG)
        instance = this

        MobclickAgent.setDebugMode(BuildConfig.DEBUG)
    }

    fun getKey():String{
        var pu = PrefUtil(this)
        return pu.getPassword()
    }

    
}

var instance:NotApplication? = null
fun getApplication(): NotApplication{
    if(instance == null){
        throw RuntimeException("app is null")
    }
    return instance!!
}
