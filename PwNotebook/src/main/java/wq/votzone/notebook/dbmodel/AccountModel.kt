package wq.votzone.notebook.dbmodel

import net.sourceforge.pinyin4j.PinyinHelper
import org.json.JSONObject
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table
import wq.votzone.notebook.enum.AccountLevel
import wq.votzone.notebook.model.AccAddItem
import wq.votzone.notebook.model.AccAddItemType

import java.util.ArrayList
import java.util.Date

import wq.votzone.notebook.util.DBUtil

import wq.votzone.notebook.util.Const.item_inner_sep_er
import wq.votzone.notebook.util.Const.item_sep_er
import wq.votzone.notebook.util.Const.sp_file_name
import java.io.Serializable

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/26
 * Modification History:
 * Why & What modified:
 */
@Table(name = "db_account_model")
class AccountModel:Serializable {
    @Column(name = "id", isId = true)
    var id = -1

    @Column(name = "platform")
    var platform = ""

    @Column(name = "name")
    var username = ""

    @Column(name = "passwd")
    var password = ""


    @Column(name = "time")
    var time = Date()

    @Column(name = "tags")
    var tags = ""

    @Column(name = "more_items")
    var moreItems = ""

    @Column(name = "level")
    private var dblevel = 0

    var level: AccountLevel
        get(){
            when(dblevel){
                AccountLevel.spam.level ->return  AccountLevel.spam
                AccountLevel.soso.level ->return  AccountLevel.soso
                AccountLevel.common.level ->return  AccountLevel.common
                AccountLevel.vip.level -> return AccountLevel.vip
                AccountLevel.autonym.level -> return AccountLevel.autonym
            }
            return AccountLevel.common
        }
        set(value) {
            dblevel = value.level
        }

    @Column(name = "alphabeta")
    private var alphabeta = "z"

    var taglist = ArrayList<Tag>()

    var otherItems = ArrayList<OtherItem>()

    override fun equals(other: Any?): Boolean {
        if(other is AccountModel ){
            return this.platform.equals(other.platform)
                    && this.username.equals(other.username)
                    && this.password.equals(other.password)
        }
        return false
    }


    /**

     * save

     * find

     * convert_to_json

     * convert_from_json

     */

    fun decodeFromDb(){
        if(tags !=null && tags.length >0) {
            for (name in tags.split(item_sep_er)) {
                var t = Tag()
                t.name = name
                taglist.add(t)
            }
        }
        if(moreItems !=null && moreItems.length >0) {
            for (item in moreItems.split(item_sep_er)) {
                var oh = OtherItem();
                oh.name = item.split(item_inner_sep_er)[0]
                oh.value = item.split(item_inner_sep_er)[1]
                otherItems.add(oh)
            }
        }
    }

    fun encode2Str(){
        var sbuilder = StringBuilder()
        for (tag in taglist) {

            sbuilder.append(tag.name).append(item_sep_er)
        }
        tags = sbuilder.toString()
        if (tags.length > 0) {
            tags = tags.substring(0, tags.length - item_sep_er.length)
        }

        sbuilder = StringBuilder()
        for (item in otherItems) {
            sbuilder.append(item.name).append(item_inner_sep_er).append(item.value).append(item_sep_er)
        }
        moreItems = sbuilder.toString()
        if (moreItems.length > 0) {
            moreItems = moreItems.substring(0, moreItems.length - item_sep_er.length)
        }

        var cr = platform[0]
        if(cr  in 'a'..'z'||cr in 'A' ..'Z'){
            alphabeta = platform
        }else if(cr in '0'..'9'){
            alphabeta = "#"
        }else{
            var c = platform[0]
            try {
                alphabeta = PinyinHelper.toHanyuPinyinStringArray(c)[0]
            } catch (e: Exception) {
                alphabeta = "#"
            }
        }
    }

    fun saveInstance() {
        encode2Str()
        if(id == -1) {
            DBUtil.saveBindingId(this)
        }else{
            DBUtil.update(this)
        }
    }

    fun delete(){
        DBUtil.delete(this)
    }

    fun getAccAddItem( pos:Int): AccAddItem?{
        when(pos){
            0 -> {
                return AccAddItem("平台", AccAddItemType.Platform, platform)
            }
            1 -> {
                return AccAddItem("账号", AccAddItemType.Username, username)
            }
            2 -> {
                return AccAddItem("密码", AccAddItemType.Password, password)
            }
            3-> {
                return AccAddItem("登记", AccAddItemType.Acclevel, level)
            }
            4 ->{
                return AccAddItem("标签", AccAddItemType.Tag, taglist)
            }
            (otherItems.size + 5)->{
                return null
            }
            else -> {
                val item = otherItems[pos -5]
                return AccAddItem(item.name, AccAddItemType.Other, item.value)
            }
        }

    }

    fun removeLastTag(){
        if(this.taglist.size >0) {

            this.taglist.remove(taglist.last())

        }
    }

    fun convert2Json():JSONObject{
        encode2Str()
        val jobj =JSONObject()
        jobj.put("platform",this.platform)
        jobj.put("alphabeta",this.alphabeta)
        jobj.put("dblevel",this.dblevel)
        jobj.put("moreitems",this.moreItems)
        jobj.put("password",this.password)
        jobj.put("tags",this.tags)
        jobj.put("username", this.username)
        return jobj
    }

    fun convertFrom(jobj:JSONObject){
        this.platform = jobj.optString("platform")
        this.alphabeta = jobj.optString("alphabeta")
        this.dblevel = jobj.optInt("dblevel")
        this.moreItems = jobj.optString("moreitems")
        this.password = jobj.optString("password")
        this.tags = jobj.optString("tags")
        this.username = jobj.optString("username")
        decodeFromDb()
    }

}

fun getAllAccount(order: String = "alphabeta"):ArrayList<AccountModel>{
    var list = ArrayList<AccountModel>()
    var findlist = DBUtil.findAll(AccountModel::class.java, order, false)
    if(findlist !=null){
        for(acc in findlist){

            acc.decodeFromDb()

            list.add(acc)
        }

    }
    return list
}

