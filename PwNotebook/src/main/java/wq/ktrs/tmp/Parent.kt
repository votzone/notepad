package wq.ktrs.tmp

import org.xutils.DbManager
import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table
import org.xutils.ex.DbException

import java.util.Date

/**
 * Author: wyouflf
 * Date: 13-7-25
 * Time: 下午7:06
 */
@Table(name = "parent", onCreated = "CREATE UNIQUE INDEX index_name ON parent(name,email)")
class Parent {

    // 一对一
    //public Child getChild(DbManager db) throws DbException {
    //    return db.selector(Child.class).where("parentId", "=", this.id).findFirst();
    //}

    @Column(name = "id", isId = true)
    var id: Int = 0

    @Column(name = "name")
    var name: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "isAdmin")
    var isAdmin: Boolean = false

    @Column(name = "time")
    var time: Date? = null

    @Column(name = "date")
    var date: java.sql.Date? = null

    @Throws(DbException::class)
    fun getChildren(db: DbManager): List<Child> {
        return db.selector(Child::class.java).where("parentId", "=", this.id).findAll()
    }

    override fun toString(): String {
        return "Parent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", time=" + time +
                ", date=" + date +
                '}'
    }
}
