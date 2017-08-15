package wq.votzone.notebook.dbmodel

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table
import wq.ktrs.tmp.Child
import wq.votzone.notebook.util.DBUtil
import java.io.Serializable

import java.util.Date

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/3
 * Modification History:
 * Why & What modified:
 */

@Table(name = "tag")
class Tag: Serializable{

    @Column(name = "id", isId = true)
    var id: Int = -1

    @Column(name = "name")
    var name: String = ""

    @Column(name = "time")
    var time: Date = Date()// 创建时间, 用于必要地方排序


    fun set2Tag(otherTag: Tag){
        otherTag.id = id
        otherTag.name = name
        otherTag.time = time
    }

    fun saveInstance(){
        DBUtil.saveBindingId(this)
    }

}

fun getAllTag():ArrayList<Tag>{
    val alltags = ArrayList<Tag>()

    val tags = DBUtil.findAll(Tag::class.java, "time", true)
    if(tags !=null){
        alltags.addAll(tags)
    }

    return alltags
}
