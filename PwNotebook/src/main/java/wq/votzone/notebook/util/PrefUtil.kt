package wq.votzone.notebook.util

import android.content.Context
import android.text.TextUtils
import wq.votzone.notebook.util.Const.First_Launch
import wq.votzone.notebook.util.Const.pass_word_key

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/6/29
 * Modification History:
 * Why & What modified:
 */
class PrefUtil(var context: Context) {

    var sp = context.getSharedPreferences(Const.sp_file_name, Context.MODE_PRIVATE)

    fun isFirstlaunch():Boolean{

        val firstlaunch = sp.getBoolean(Const.First_Launch, true)
        if(firstlaunch) {
            var ed = sp.edit()
            ed.putBoolean(First_Launch, false)
            ed.commit()
        }
        return  firstlaunch
    }

    fun needShowPassHint():Boolean{
        return sp.getBoolean(Const.Need_Show_PassHint, true)
    }

    fun setNoNeedShowPassHint(){
        var ed = sp.edit()
        ed.putBoolean(Const.Need_Show_PassHint, false)
        ed.commit()
    }

    fun getPassword():String{
        return sp.getString(Const.pass_word_key, "")
    }

    fun canVerifyPass():Boolean{
        return !TextUtils.isEmpty(getPassword())
    }

    fun verifyPass(inpass: String):Boolean{
        return TextUtils.equals(getPassword(), inpass)
    }

    fun setPassword(passd: String){
        var ed = sp.edit()
        ed.putString(Const.pass_word_key, passd)
        ed.commit()
    }

    fun canShowLoanMarket():Boolean{
        // 大于5天 并且 使用次数大于25次
        var crt = System.currentTimeMillis()
        var tm = sp.getLong("can_show_loan_market",crt)
        var b1 =  Math.abs(tm - crt) > 5*24* 60 * 60 *1000

        var last = sp.getInt("use_times", 1)
        var b2 = last > 25
        var ed = sp.edit()
        ed.putInt("use_times", last+1)
        ed.commit()
        return b1 && b2
    }
}